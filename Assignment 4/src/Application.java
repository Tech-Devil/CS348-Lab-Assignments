import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

public class Application {

    private static Node node;
    private static String knownNode;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String ip = null;

        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.getHostAddress().startsWith("172.16.")) {
                        System.out.println("IP -----> " + i.getHostAddress());
                        ip = i.getHostAddress();
                    }
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        if (ip == null) {
            System.out.println("Ip issues, stoping !!");
            System.exit(0);
        }

        System.out.println("Enter port = ");

        int port = sc.nextInt();
        String ip_port = ip + ":" + port;

        int hash = Helper.hashIt(ip_port);
        System.out.println("hash = " + hash);

        node = new Node(ip_port);

        System.out.println("Enter known node ip with port");

        knownNode = sc.next();

        hash = Helper.hashIt(knownNode);
        System.out.println("hash known node = " + hash);

        // TODO: 3/2/18 make a check join function
        boolean join = node.join(knownNode);

        if (join)
            System.out.println("Joined !!");
        else {
            System.out.println("Join unsuccessful !!");
            System.out.println("Creating our own network");
//            node.stop();
//            System.exit(0);
        }


        while (true) {
            System.out.println("\n\nEnter as follows --->");
            System.out.println("1 - Own IP address and ID");
            System.out.println("2 - IP address and ID of the successor and predecessor");
            System.out.println("3 - File key IDs contained");
            System.out.println("4 - Finger table");
            System.out.println("5 - Quit");
            System.out.print("Enter : ");
            int c = sc.nextInt();

            switch (c) {
                case 1: {
                    System.out.println("IP address : " + node.getLocalAddress().split(":")[0]);
                    System.out.println("Port : " + node.getLocalAddress().split(":")[1]);
                    System.out.println("ID : " + node.getId());
                    break;
                }
                case 2: {
                    String successor = node.getSuccessor();
                    String predecessor = node.getPredecessor();
                    if ((successor == null || node.getSuccessor().equals(node.getLocalAddress()))
                            && (predecessor == null || predecessor.equals(node.getLocalAddress()))) {
                        System.out.println("You are your own successor as well as predecessor");
                    } else {
                        if (successor == null)
                            System.out.println("Successor is updating.");
                        else {
                            System.out.println("Successor Ip : " + successor + " and its ID - " + Helper.hashIt(successor));
                        }
                        if (successor == null)
                            System.out.println("Predecessor is updating.");
                        else {
                            System.out.println("Predecessor Ip : " + predecessor + " and its ID - " + Helper.hashIt(predecessor));
                        }
                    }
                    break;
                }
                case 3: {
                    // TODO: 3/2/18
                    File[] files = new File("files").listFiles();

                    if (files == null)
                        System.out.println("No files present");
                    else {
                        for (int i = 0; i < files.length; i++) {
                            System.out.println(files[i].getName() + " " + Helper.hashIt(files[i].getName()));
                        }
                    }

                    break;
                }
                case 4: {
                    String[] fingerTable = node.getFingerTable();
                    System.out.println("Finger table --->");
                    for (int i = 0; i < fingerTable.length; i++) {
                        System.out.println(i + " - " + fingerTable[i]);
                    }
                    break;
                }
                case 5: {
                    node.stop();
                    System.exit(0);
                }
            }
        }


    }
}