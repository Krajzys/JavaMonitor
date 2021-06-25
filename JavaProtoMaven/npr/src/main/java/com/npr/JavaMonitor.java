package com.npr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.Comparator;

import com.google.protobuf.InvalidProtocolBufferException;
import com.npr.Message.Lamport;
import com.npr.Message.Lamport.Builder;
import com.npr.Message.Lamport.MessageType;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class JavaMonitor {
    private final int BROADCAST_ID = -10;
    private ArrayList<String> addresses;
    private ArrayList<Integer> sleepList;
    private PriorityQueue<Lamport> priorityList;
    private Map<Integer, Integer> initArray;
    private Map<Integer, Boolean> acksMap;
    private Object lockPhase = new Object();
    private Object lockInit = new Object();
    private Object lockClock = new Object();
    private Object lockAcks = new Object();
    private Object lockPrio = new Object();
    private Object lockSleep = new Object();
    private Semaphore lockPreInit = new Semaphore(0);
    private Semaphore criticalSection = new Semaphore(0);
    private Semaphore sleeping = new Semaphore(0);
    private int port;
    private long lamportClock = 0;
    private boolean communicationInitialized = false;
    private volatile boolean communicationShouldEnd = false;
    private int initializationPhase = 0;
    private boolean wantsCriticalSection = false;
    private ZContext context;

    // może rozszerzyć inicjalizację o error (jak ktoś użył 2 takich samych portów)

    ReceiverMontor receiver;
    SenderMonitor sender;

    private class ReceiverMontor extends Thread {
        private ArrayList<String> addresses;
        private ZMQ.Socket rsocket;

        ReceiverMontor(ArrayList<String> addresses) {
            this.addresses = addresses;
        }

        private void tryEnterCriticalSection() {
            boolean acksOk = false;
            synchronized (lockAcks) {
                acksOk = (getAcks() == addresses.size());
            }
            boolean prioOk = false;
            synchronized (lockPrio) {
                prioOk = (wantsCriticalSection && priorityList.size() > 0 && priorityList.peek().getIdSender() == port);
            }
            if (acksOk && prioOk) {
                System.out.println("[INFO] "+ port + ") Entering critical section");
                criticalSection.release();
            }
        }

        @Override
        public void run() {
            rsocket = context.createSocket(SocketType.SUB);
            for (String address: this.addresses) {
                rsocket.connect(address);
                rsocket.subscribe("");
            }
            System.out.println("[INFO] " + port + ") Created ReceiverMonitor");
            
            while(!communicationShouldEnd) {
                System.err.println("[DEBUG] " + port + ") Waiting for message...");
                Lamport message = recvMessage();
                if (message == null) continue;

                if (!communicationInitialized) {
                    if (message.getMessageType() == MessageType.REQUEST_INITIAL && !initArray.containsKey(message.getIdSender())) {
                        initArray.put(message.getIdSender(), 1);
                    } else if (message.getMessageType() == MessageType.REPLY_INITIAL) {
                        initArray.put(message.getIdSender(), 2);
                    } else if (message.getMessageType() == MessageType.RELEASE_INITIAL) {
                        synchronized (lockInit) {
                            communicationInitialized = true;
                            System.out.println("[INFO] " + port + ") Initialization algorithm finished!");
                        }
                    }

                    int phaseOnes = 0;
                    int phaseTwos = 0;
                    for (Integer k: initArray.keySet()) {
                        if (initArray.get(k) > 0) {
                            phaseOnes += 1;
                        }
                        if (initArray.get(k) == 2) {
                            phaseTwos += 1;
                        }
                    }
                    
                    synchronized(lockPhase) {
                        System.out.println("[DEBUG] " + port + ") phaseOne: " + phaseOnes + "; phaseTwo: " + phaseTwos + "; size:" + addresses.size());
                        if (phaseOnes == addresses.size() && initializationPhase < 1) {
                            initializationPhase = 1;
                            System.out.println("[INFO] " + port + ") Changing phase to 1 (sending REPLY)");
                        }
                        if (phaseTwos == addresses.size()) {
                            initializationPhase = 2;
                            System.out.println("[INFO] " + port + ") Changing phase to 2 (sending RELEASE)");
                        }
                    }
                } else {
                    // filter out messages
                    int idReceiver = message.getIdReceiver();
                    MessageType mType = message.getMessageType();
                    if (idReceiver != BROADCAST_ID && idReceiver != port && mType != MessageType.WAKE) {
                        continue;
                    }
                    if (mType == MessageType.REQUEST) {
                        synchronized (lockPrio) {
                            priorityList.add(message);
                        }
                        Lamport reply = createMessage(message.getIdSender(), MessageType.REPLY);
                        sendAll(reply);
                    }
                    else if (mType == MessageType.REPLY) {
                        synchronized (lockAcks) {
                            acksMap.replace(message.getIdSender(), true);
                        }

                        System.out.print("[DEBUG] " + port + ") ");
                        int tid = 0;
                        for (Lamport mesg: priorityList) {
                            System.out.println(tid + " CLOCK:" + mesg.getClock() + " IDSENDER:" + mesg.getIdSender());
                            tid += 1;
                        }

                        tryEnterCriticalSection();
                    }
                    else if (mType == MessageType.RELEASE) {
                        synchronized (lockPrio) {
                            if (priorityList.size() > 0 && priorityList.peek().getIdSender() == message.getIdSender())
                                priorityList.remove();
                        }
                        tryEnterCriticalSection();
                    }
                    else if (mType == MessageType.END_COMMUNICATION) {
                        String toRemove = null;
                        for (String addr: addresses) {
                            if (addr.endsWith(String.valueOf(message.getIdSender()))) {
                                toRemove = addr;
                            }
                        }
                        if (toRemove != null && addresses.contains(toRemove)) {
                            addresses.remove(toRemove);
                        }
                        synchronized (acksMap) {
                            if (acksMap.containsKey(message.getIdSender())) {
                                acksMap.remove(message.getIdSender());
                            }
                        }

                        Lamport endMessage = createMessage(message.getIdSender(), MessageType.END_COMMUNICATION);
                        sendAll(endMessage);
                    }
                    else if (mType == MessageType.SLEEP) {
                        synchronized (lockSleep) {
                            sleepList.add(message.getIdSender());
                        }
                        synchronized (lockPrio) {
                            if (priorityList.size() > 0 && message.getIdSender() == priorityList.peek().getIdSender()) {
                                priorityList.remove();
                            }
                        }
                        tryEnterCriticalSection();
                    }
                    else if (mType == MessageType.WAKE) {
                        synchronized (lockSleep) {
                            if (sleepList.contains(idReceiver)) {
                                sleepList.remove(Integer.valueOf(idReceiver));
                            }
                            else if (idReceiver == BROADCAST_ID) {
                                sleepList.clear();
                            }
                        }

                        if (idReceiver == port || idReceiver == BROADCAST_ID) {
                            if (sleeping.availablePermits() == 0) {
                                sleeping.release();
                            }
                        }
                    }
                }
            }
        }

        private Lamport recvMessage(){
            byte[] serialized;
            if (!addresses.isEmpty()) {
                serialized = rsocket.recv();
            }
            else {
                communicationShouldEnd = true;
                criticalSection.release();
                return null;
            }
            Lamport message;
            try {
                message = Lamport.parseFrom(serialized);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                return null;
            }

            System.out.println("[DEBUG] " + port + ") received message: from " + message.getIdSender() + "; to " + message.getIdReceiver() + "; type " + message.getMessageType().name() + "; clock " + message.getClock() + "; acks " + getAcks());

            synchronized (lockClock) {
                lamportClock = Long.max(message.getClock(), lamportClock) + 1;
            }

            return message;
        }
    }

    private class SenderMonitor extends Thread {
        private ZMQ.Socket pubSocket;
        // private int port;

        // SenderMonitor() {}

        @Override
        public void run() {
            pubSocket = context.createSocket(SocketType.PUB);
            pubSocket.bind("tcp://*:" + port);

            System.out.println("[INFO] " + port + ") Created SenderMonitor");
            this.initializtionAlgoritm();

            synchronized (lockAcks) {
                for (Integer key: initArray.keySet()) {
                    acksMap.put(key, false);
                }
            }

            lockPreInit.release();
        }

        public boolean initializtionAlgoritm() {
            while (!communicationInitialized) {
                Lamport message;
                int localInitializationPhase = 0;
                synchronized (lockPhase) {
                    localInitializationPhase = initializationPhase;
                }
                if (localInitializationPhase == 0) {
                    message = createMessage(BROADCAST_ID, MessageType.REQUEST_INITIAL);
                    System.out.println("[DEBUG] " + port + ") Sending REQUEST!");
                }
                else if (localInitializationPhase == 1) {
                    message = createMessage(BROADCAST_ID, MessageType.REPLY_INITIAL);
                    System.out.println("[DEBUG] " + port + ") Sending REPLY!");
                }
                else if (localInitializationPhase == 2) {
                    message = createMessage(BROADCAST_ID, MessageType.RELEASE_INITIAL);
                    System.out.println("[DEBUG] " + port + ") Sending RELEASE!");
                    synchronized (lockInit) {
                        communicationInitialized = true;
                        System.out.println("[INFO] " + port + ") Initialization algorithm finished!");
                    }
                }
                else {
                    return false;
                }
                
                sendAll(message);

                try {
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        public boolean sendAll(Lamport message) {
            byte[] serialized = message.toByteArray();

            synchronized (lockClock) {
                lamportClock += 1;
            }
            
            return pubSocket.send(serialized);
        }
    }

    private class CompareLamport implements Comparator<Lamport> {
        @Override
        public int compare(Lamport m1, Lamport m2) {
            if (m1.getClock() > m2.getClock()) {
                return 1;
            }
            else if (m1.getClock() < m2.getClock()) {
                return -1;
            }
            if ((m1.getClock() == m2.getClock()) && (m1.getIdSender() < m2.getIdSender())) {
                return 1;
            }
            else if ((m1.getClock() == m2.getClock()) && (m1.getIdSender() > m2.getIdSender())) {
                return -1;
            }
            else { 
                return 0;
            }
        }
    }

    public JavaMonitor(int id) {
        this.context = new ZContext();
        this.addresses = new ArrayList<String>();
        this.priorityList = new PriorityQueue<Lamport>(new CompareLamport());
        this.port = id;
        this.initArray = new HashMap<Integer, Integer>();
        this.sleepList = new ArrayList<Integer>();
        this.acksMap = new HashMap<Integer, Boolean>();
    }

    public JavaMonitor(int id, String[] addresses) {
        this.context = new ZContext();
        this.addresses = new ArrayList<String>();
        this.priorityList = new PriorityQueue<Lamport>(new CompareLamport());
        for (String address: addresses) {
            this.addresses.add(address);
        }
        
        this.port = id;
        this.initArray = new HashMap<Integer, Integer>();
        this.sleepList = new ArrayList<Integer>();
        this.acksMap = new HashMap<Integer, Boolean>();
    }

    public boolean addAddress(String address) {
        return this.addresses.add(address);
    }

    public void printAddresses() {
        for (String address: this.addresses) {
            System.out.println(address);
        }
    }

    private int getAcks() {
        int acks = 0;
        synchronized (lockAcks) {
            for (Integer key: acksMap.keySet()) {
                if (acksMap.get(key)) {
                    acks += 1;
                }
            }
        }

        return acks;
    }

    private void clearAcks() {
        synchronized (lockAcks) {
            for (Integer key: acksMap.keySet()) {
                acksMap.put(key, false);
            }
        }
    }

    public boolean initializeCommunication() {
        this.receiver = new ReceiverMontor(this.addresses);
        this.sender = new SenderMonitor();
        this.receiver.start();
        this.sender.start();

        return true;
    }

    public boolean endCommunication() {
        communicationShouldEnd = true;
        Lamport message = createMessage(BROADCAST_ID, MessageType.END_COMMUNICATION);
        sendAll(message);

        try {
            this.receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            this.sender.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean sendAll(Lamport message) {
        return sender.sendAll(message);
    }
    
    public Lamport createMessage(int receiverId, MessageType type) {
        Builder message = Lamport.newBuilder();

        message.setIdReceiver(receiverId);
        message.setIdSender(this.port);
        message.setMessageType(type);
        synchronized(this.lockClock) {
            message.setClock(this.lamportClock);
        }

        return message.build();
    }

    public void beginSynchronized() {
        boolean isCommunicationInitialized = true;
        synchronized (lockInit) {
            isCommunicationInitialized = communicationInitialized;
        }
        if (!isCommunicationInitialized) {
            try {
                this.lockPreInit.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        clearAcks();
        Lamport message = createMessage(BROADCAST_ID, MessageType.REQUEST);
        synchronized (lockPrio) {
            priorityList.add(message);
        }
        wantsCriticalSection = true;
        System.err.println("sending request");
        sendAll(message);

        try {
            criticalSection.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void endSynchronized() {
        Lamport message = createMessage(BROADCAST_ID, MessageType.RELEASE);
        wantsCriticalSection = false;
        synchronized (lockPrio) {
            if (priorityList.size() > 0 && priorityList.peek().getIdSender() == port)
                priorityList.remove();
        }
        criticalSection.release();
        sendAll(message);
        System.out.println("[INFO] " + port + ") Releasing critical section");
    }

    public void blockWait() {
        Lamport message = createMessage(BROADCAST_ID, MessageType.SLEEP);
        synchronized (lockSleep) {
            sleepList.add(port);
        }
        synchronized (lockPrio) {
            if (priorityList.peek().getIdSender() == port)
                priorityList.remove();
        }
        sendAll(message);
        try {
            sleeping.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Trying to acquire section again!");
        beginSynchronized();
    }

    public void signal() {
        int idToWake = 0;
        boolean someoneIsSleeping = false;
        synchronized (lockSleep) {
            if (sleepList.size() > 0) {
                idToWake = sleepList.get(0);
                someoneIsSleeping = true;
            }
        }
        if (someoneIsSleeping) {
            Lamport message = createMessage(idToWake, MessageType.WAKE);
            sendAll(message);
        }
    }

    public void signalAll() {
        synchronized (lockSleep) {
            sleepList.clear();
        }
        Lamport message = createMessage(BROADCAST_ID, MessageType.WAKE);
        sendAll(message);
    }
}