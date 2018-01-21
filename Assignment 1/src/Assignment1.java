import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Assignment1 {

    public static void main(String args[]) {


        System.out.print("Enter the number of sources : ");
        Scanner sc = new Scanner(System.in);
        int sourceNum = sc.nextInt();

        System.out.print("Enter the packet Length : ");
        int packetLength = sc.nextInt();

        try {
            FileWriter fw = new FileWriter("graph1.txt", false);
            FileWriter fw2 = new FileWriter("graph2.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (float i = 5; i < 1000; i += 0.1) {
            calc(i, sourceNum, packetLength, false);
        }


        for (float i = 5; i < 1000; i += 0.1) {
            calc(i, sourceNum, packetLength, true);
        }

        String[] cmdScript = new String[]{"/bin/bash", "gnuplot.sh"};
        try {
            Runtime.getRuntime().exec(cmdScript);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void calc(double bandwidth, int sourceNum, int packetLength, boolean sizeLimit) {

        boolean lastOut = false;
        double lastOutTime = 0;

        double startTime = 0, endTime = 600;

        int packetLoss = 0, queueLength = 100, sourceId = 501;

        HashMap<Integer, Link> linkHashMap = new HashMap<>();

        linkHashMap.put(11, new Link(11, 10));
        linkHashMap.put(12, new Link(12, bandwidth));

        HashMap<Integer, Switch> switchHashMap = new HashMap<>();
        PriorityQueue<Event> switchPriorityQueue = new PriorityQueue<>();


        switchHashMap.put(1001, new Switch(1001, switchPriorityQueue, 12));


        HashMap<Integer, Source> sourceHashMap = new HashMap<>();


        int packet_Id = 123;

        HashMap<Integer, Packet> packetHashMap = new HashMap<>();


//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 2, t2, 502, 0));
//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 2, t3, 503, 0));
//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 2, t4, 504, 0));

//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 100, t1, 501, 0));
//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 100, t2, 502, 0));
//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 100, t3, 503, 0));
//        packetHashMap.put(packet_Id, new Packet(packet_Id++, 100, t4, 504, 0));

        PriorityQueue<Event> eventPriorityQueue = new PriorityQueue<>();


//        eventPriorityQueue.add(new Event(0, t2, 124));
//        eventPriorityQueue.add(new Event(0, t3, 125));
//        eventPriorityQueue.add(new Event(0, t4, 126));

//        for (Event e : eventPriorityQueue) {
//            System.out.println("Type = " + e.getType() + " timestamp = " + e.getTimestamp() + " packetId = " + e.getPacketId());
//        }

//        System.out.println(packetHashMap.toString());


        for (int i = 0; i < sourceNum; i++) {

            double randomNum = ThreadLocalRandom.current().nextDouble(0, 1);
            sourceHashMap.put(sourceId, new Source(sourceId, 2, 11, 1001));
            packetHashMap.put(packet_Id, new Packet(packet_Id, packetLength, randomNum, sourceId++, 0));
            eventPriorityQueue.add(new Event(0, randomNum, packet_Id++));

        }


        while (!eventPriorityQueue.isEmpty() && eventPriorityQueue.peek().getTimestamp() < endTime) {
            Event event = eventPriorityQueue.poll();
            Packet packet = packetHashMap.get(event.getPacketId());
            Source source = sourceHashMap.get(packet.getSourceId());

//            System.out.println("\n\n\nWhile starts here ------->");
//            System.out.println(event.getPacketId() + " " + event.toString());
//            System.out.println("Event Priority Queue  -----> " + eventPriorityQueue.size());
//            System.out.println(eventPriorityQueue.toString());

            switch (event.getType()) {

                case 0: {

//                    System.out.println("case 0");
                    double lambda = source.getPacketGenerationRate();

                    // Creating new packet
//                    packetHashMap.put(packet_Id,
//                            new Packet(packet_Id,
//                                    100,
//                                    packet.getCreationTimestamp() + lambda,
//                                    packet.getSourceId(),
//                                    0));
                    packetHashMap.put(packet_Id,
                            source.generatePacket(packet_Id,
                                    packet.getPacketLength(),
                                    packet.getCreationTimestamp() + lambda));

                    // Creating event E0
                    eventPriorityQueue.add(
                            new Event(0, packet.getCreationTimestamp() + lambda, packet_Id++)
                    );

                    // Creating event E1

                    PriorityQueue<Event> queue = switchHashMap.get(source.getSwitchId()).getEventPriorityQueue();

//                    System.out.println("Queue size = " +queue.size());

                    if (sizeLimit && queue.size() >= queueLength) {
                        packetLoss++;
                        break;
                    }


                    double bs = linkHashMap.get(source.getLinkId()).getBandwidth();

                    double l_bs = ((double) packet.getPacketLength()) / bs;

                    eventPriorityQueue.add(
                            new Event(1, packet.getCreationTimestamp() + l_bs, packet.getPacketID())
                    );


                    queue.add(new Event(1, packet.getCreationTimestamp() + l_bs, packet.getPacketID()));
                    break;
                }

                case 1: {
//                    System.out.println("case 1");
                    long n_l = 0;
                    PriorityQueue<Event> queue = switchHashMap.get(source.getSwitchId()).getEventPriorityQueue();
//                    System.out.println("event small queue ---> ");
//                    System.out.println(queue.toString());


                    for (Event event1 : queue) {
                        if (event1.getTimestamp() < event.getTimestamp())
                            n_l += packetHashMap.get(event1.getPacketId()).getPacketLength();
                    }
                    double bss = linkHashMap.get(switchHashMap.get(source.getSwitchId()).getLinkId()).getBandwidth();
                    double n_l_bss = ((double) n_l )/ bss;

                    double tx = 0;

                    if (lastOut) {
                        double tmp = (double) packetHashMap.get(event.getPacketId()).getPacketLength() / bss;
                        tx = tmp - (event.getTimestamp() - lastOutTime);
                        if (tx < 0){
                            tx = 0;
                        }

                    }

//                    for (Event event1 : eventPriorityQueue) {
//                        if (event1.getType() == 2) {
//                            double tmp = packetHashMap.get(event1.getPacketId()).getPacketLength() / bss;
//                            double tmp2 = tmp - (event.getTimestamp() - event1.getTimestamp());
//                            if (tmp2 < tx || tx == 0) {
//                                tx = tmp2;
////                                System.out.println("***************** Tx = " + tx);
////                                System.out.println("tmp = " + tmp + " event = " + event.getTimestamp() + " event1 = " + event1.getTimestamp() + "tx = " + tx);
//                            }
//                        } else if (event1.getType() == 3) {
////                            double tmp = packetHashMap.get(event1.getPacketId()).getPacketLength() / bss;
//                            double tmp2 = (event1.getTimestamp() - event.getTimestamp());
//                            if (tmp2 < tx || tx == 0) {
//                                tx = tmp2;
////                                System.out.println("**************333 Tx = " + tx);
////                                System.out.println(" event = " + event.getTimestamp() + " event1 = " + event1.getTimestamp() + "tx = " + tx);
//                            }
//                        }
//                    }

//                    System.out.println("Final tx = " + tx + " n_l_bss = " + n_l_bss);

                    eventPriorityQueue.add(
                            new Event(2, event.getTimestamp() + n_l_bss + tx, packet.getPacketID())
                    );
                    break;

                }

                case 2: {
//                    System.out.println("case 2");
                    double bss = linkHashMap.get(switchHashMap.get(source.getSwitchId()).getLinkId()).getBandwidth();
                    double l_bss = (double) packetHashMap.get(event.getPacketId()).getPacketLength() / bss;

                    packet.setDeletiontimestamp(event.getTimestamp() + l_bss);

                    PriorityQueue<Event> queue = switchHashMap.get(source.getSwitchId()).getEventPriorityQueue();

                    queue.poll();

                    lastOut = true;
                    lastOutTime = event.getTimestamp();

                    eventPriorityQueue.add(
                            new Event(3, event.getTimestamp() + l_bss, packet.getPacketID())
                    );

                    break;

                }

                case 3: {
                    // TODO: 1/20/18
                    break;
                }

                default: {
                    System.out.println("It should not reach here");
                    break;
                }
            }

//            System.out.println("Just before while ends ------->");
//            System.out.println("Event Priority Queue  -----> " + eventPriorityQueue.size());
//            System.out.println(eventPriorityQueue.toString());
        }

//        System.out.println("Packet Hash Map ----->");
//        System.out.println(packetHashMap.toString());

        double totalDelay = 0;
        int count = 0, generated = 0;

        for (Object o : packetHashMap.entrySet()) {
            HashMap.Entry pair = (Map.Entry) o;
            Packet packet = (Packet) pair.getValue();
            if (packet.getDeletiontimestamp() != 0 && packet.getDeletiontimestamp() < endTime) {
                totalDelay += (packet.getDeletiontimestamp() - packet.getCreationTimestamp());
                count++;
//                System.out.println(packet.toString());
            }

            if (packet.getCreationTimestamp() < endTime) {
                generated++;
            }

//            it.remove(); // avoids a ConcurrentModificationException
        }


//        System.out.println(packetHashMap.toString());
//
//        System.out.println("Event Priority Queue ----->");
//        System.out.println(eventPriorityQueue.toString());
        if (count > 0) {

            double tmp = ((double)packetLoss / generated);
//            System.out.println("count = "+count+" packetloss = "+packetLoss+" generated = "+generated+ " tmp = "+tmp+" f = " +(double)(packetLoss/generated));

            String data = "";
            if (sizeLimit)
                data = tmp + " " + (sourceNum * 0.5 * packetLength / bandwidth);
            else
                data = (totalDelay / ((double)count)) + " " + (sourceNum * 0.5 * packetLength / bandwidth);

//            System.out.println("Average delay = " + (double) (totalDelay / count) + " utilization factor = " + (double) (4 * 0.5 * 2 / bandwidth));

//            OutputStream os = null;
//            try {
//                os = new FileOutputStream(new File("graph1.txt"));
//                os.write(data.getBytes(), 0, data.length());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally{
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            String filename = "";

            if (sizeLimit)
                filename = "graph2.txt";
            else
                filename = "graph1.txt";


            try (FileWriter fw = new FileWriter(filename, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(data);
                //more code
//                out.println("more text");
                //more code
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }


        }

//        System.out.println("Done");

    }
}
