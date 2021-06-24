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
    private Object lockPhase = new Object();
    private Object lockInit = new Object();
    private Object lockClock = new Object();
    private Object lockAcks = new Object();
    private Object lockPrio = new Object();
    private Object lockSleep = new Object();
    private Semaphore lockPreInit = new Semaphore(0);
    private Semaphore criticalSection = new Semaphore(0);
    private Semaphore sleeping = new Semaphore(0);
    private int id;
    private long lamportClock = 0;
    private boolean communicationInitialized = false;
    private boolean communicationShouldEnd = false;
    private int initializationPhase = 0;
    private boolean wantsCriticalSection = false;
    private ZContext context;
    private int acks;

    // id nadawane po 

    ReceiverMontor receiver;
    SenderMonitor sender;

    private class ReceiverMontor extends Thread {
        private ArrayList<String> addresses;
        // private ArrayList<ZMQ.Socket> subSockets;
        private ZMQ.Socket rsocket;

        ReceiverMontor(ArrayList<String> addresses) {
            this.addresses = addresses;
        }

        private void tryEnterCriticalSection() {
            boolean acksOk = false;
            synchronized (lockAcks) {
                acksOk = (acks == addresses.size());
            }
            boolean prioOk = false;
            synchronized (lockPrio) {
                prioOk = (wantsCriticalSection && priorityList.size() > 0 && priorityList.peek().getIdSender() == id);
            }
            if (acksOk && prioOk) {
                System.out.println("[INFO] "+ id + ") Entering critical section");
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
            System.out.println("[INFO] " + id + ") Created ReceiverMonitor");
            
            while(!communicationShouldEnd) {
                System.err.println("[DEBUG] " + id + ") Waiting for message...");
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
                            System.out.println("[INFO] " + id + ") Initialization algorithm finished!");
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
                        System.out.println("[DEBUG] " + id + ") phaseOne: " + phaseOnes + "; phaseTwo: " + phaseTwos + "; size:" + addresses.size());
                        if (phaseOnes == addresses.size() && initializationPhase < 1) {
                            initializationPhase = 1;
                            System.out.println("[INFO] " + id + ") Changing phase to 1 (sending REPLY)");
                        }
                        if (phaseTwos == addresses.size()) {
                            initializationPhase = 2;
                            System.out.println("[INFO] " + id + ") Changing phase to 2 (sending RELEASE)");
                        }
                    }
                } else {
                    // filter out messages
                    int idReceiver = message.getIdReceiver();
                    MessageType mType = message.getMessageType();
                    if (idReceiver != BROADCAST_ID && idReceiver != id && mType != MessageType.WAKE) {
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
                            acks += 1;
                        }

                        System.out.print("[DEBUG] " + id + ") ");
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
                        communicationShouldEnd = true;
                        criticalSection.release();
                        Lamport endMessage = createMessage(BROADCAST_ID, MessageType.END_COMMUNICATION);
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

                        if (idReceiver == id || idReceiver == BROADCAST_ID) {
                            sleeping.release();
                        }
                    }
                }
            }
        }

        private Lamport recvMessage() {
            byte[] serialized = rsocket.recv();
            Lamport message;
            try {
                message = Lamport.parseFrom(serialized);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                return null;
            }

            synchronized (lockAcks) {
                System.out.println("[DEBUG] " + id + ") received message: from " + message.getIdSender() + "; to " + message.getIdReceiver() + "; type " + message.getMessageType().name() + "; clock " + message.getClock() + "; acks " + acks);
            }

            synchronized (lockClock) {
                lamportClock = Long.max(message.getClock(), lamportClock) + 1;
            }

            return message;
        }
    }

    private class SenderMonitor extends Thread {
        private ZMQ.Socket pubSocket;
        private int port;

        SenderMonitor(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            pubSocket = context.createSocket(SocketType.PUB);
            pubSocket.bind("tcp://*:" + port);

            System.out.println("[INFO] " + id + ") Created SenderMonitor");
            this.initializtionAlgoritm();
            lockPreInit.release();
            System.out.println("Released preinit " + lockPreInit.availablePermits());
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
                    System.out.println("[DEBUG] " + id + ") Sending REQUEST!");
                }
                else if (localInitializationPhase == 1) {
                    message = createMessage(BROADCAST_ID, MessageType.REPLY_INITIAL);
                    System.out.println("[DEBUG] " + id + ") Sending REPLY!");
                }
                else if (localInitializationPhase == 2) {
                    message = createMessage(BROADCAST_ID, MessageType.RELEASE_INITIAL);
                    System.out.println("[DEBUG] " + id + ") Sending RELEASE!");
                    synchronized (lockInit) {
                        communicationInitialized = true;
                        System.out.println("[INFO] " + id + ") Initialization algorithm finished!");
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
        this.id = id;
        this.initArray = new HashMap<Integer, Integer>();
        this.sleepList = new ArrayList<Integer>();
    }

    public JavaMonitor(int id, String[] addresses) {
        this.context = new ZContext();
        this.addresses = new ArrayList<String>();
        this.priorityList = new PriorityQueue<Lamport>(new CompareLamport());
        for (String address: addresses) {
            this.addresses.add(address);
        }
        
        this.id = id;
        this.initArray = new HashMap<Integer, Integer>();
        this.sleepList = new ArrayList<Integer>();
    }

    public boolean addAddress(String address) {
        return this.addresses.add(address);
    }

    public void printAddresses() {
        for (String address: this.addresses) {
            System.out.println(address);
        }
    }

    public boolean initializeCommunication(int port) {
        this.receiver = new ReceiverMontor(this.addresses);
        this.sender = new SenderMonitor(port);
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
        message.setIdSender(this.id);
        message.setMessageType(type);
        synchronized(this.lockClock) {
            message.setClock(this.lamportClock);
        }

        return message.build();
    }

    public void beginSynchronized() {
        try {
            this.lockPreInit.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (lockAcks) {
            acks = 0;
        }
        Lamport message = createMessage(BROADCAST_ID, MessageType.REQUEST);
        synchronized (lockPrio) {
            priorityList.add(message);
        }
        wantsCriticalSection = true;
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
            if (priorityList.size() > 0 && priorityList.peek().getIdSender() == id)
                priorityList.remove();
        }
        sendAll(message);
        System.out.println("[INFO] " + id + ") Releasing critical section");

        // TODO: Implement this
        return ;
    }

    public void blockWait() {
        Lamport message = createMessage(BROADCAST_ID, MessageType.SLEEP);
        synchronized (lockSleep) {
            sleepList.add(id);
        }
        synchronized (lockPrio) {
            if (priorityList.peek().getIdSender() == id)
                priorityList.remove();
        }
        sendAll(message);
        try {
            sleeping.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
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