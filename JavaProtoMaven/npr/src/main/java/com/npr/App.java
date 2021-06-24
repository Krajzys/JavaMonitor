package com.npr;

public class App 
{
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please specify at least id, port and one address");
        }

        String[] arrs = new String[args.length - 2];
        int i = 0;
        for (String addr: args) {
            if (i > 1) {
                arrs[i-2] = addr;
            }
            i++;
        }

        int id = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        JavaMonitor monitor = new JavaMonitor(id, arrs);
        monitor.initializeCommunication(port);

        if (id == 100) {
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
            if (i == 10 && (id == 200 || id == 300)) {
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
        if (id == 100)
            monitor.signalAll();
        monitor.endSynchronized();
    }
}
