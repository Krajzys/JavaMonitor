package com.npr;

public class App 
{
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please specify at least port(id) and one address");
        }

        String[] arrs = new String[args.length - 1];
        int i = 0;
        for (String addr: args) {
            if (i >= 1) {
                arrs[i-1] = addr;
            }
            i++;
        }

        int port = Integer.parseInt(args[0]);
        int id = port;
        JavaMonitor monitor = new JavaMonitor(port, arrs);
        monitor.initializeCommunication();

        if (id == 1234) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        monitor.beginSynchronized();
        for (i = 0; i < 20; i++) {
            System.out.println("Im doing something...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 10 && (id == 1235 || id == 1236)) {
                System.out.println(id + ") I sleep...");
                monitor.blockWait();
                System.out.println(id + ") I'm waking up, I feel it in my bones...");
            }
        }
        System.out.println("Im holding critical section for no reason...\n\n\n");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id == 1234)
            monitor.signalAll();
        monitor.endSynchronized();

        monitor.endCommunication();
    }
}
