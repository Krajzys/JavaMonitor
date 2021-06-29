package com.npr;

public class App 
{
    public static void sleep(int msec) {
        try {
            Thread.sleep(msec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        sleep(5000);
        monitor.beginSynchronized();
        
        for (i = 0; i< 5; i++)
            System.out.println("IMIN");
        sleep(2000);
        for (i = 0; i< 5; i++)
            System.out.println("IMFREEINGOTHERS");

        monitor.signalAll();
        monitor.endSynchronized();

        monitor.endCommunication();
    }
}
