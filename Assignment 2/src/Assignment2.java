import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Assignment2 {

    public static void main(String args[]) {


        System.out.print("Enter the number of sources : ");
        Scanner sc = new Scanner(System.in);
        int sourceNum = sc.nextInt();

        double lambdas[] = new double[sourceNum];

        System.out.print("Enter the packet Length : ");
        int packetLength = sc.nextInt();

        try {
            new FileWriter("graph1.txt", false);
            new FileWriter("graph2.txt", false);
            new FileWriter("graph3.txt", false);
            new FileWriter("graph4.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        double tmp[] = new double[sourceNum];
        for (double i = 0.1; i < 5; i += 0.05) {

            double sumDelay = 0, sumQueueSize = 0;

            for (int j = 0; j < 100; j++) {

                for (int k = 0; k < sourceNum; k++) {
                    lambdas[k] = i;
                }

                sumDelay += calc(sourceNum, lambdas, packetLength, true, false, false, tmp, tmp);
                sumQueueSize += calc(sourceNum, lambdas, packetLength, false, false, false, tmp, tmp);

            }

            String data1 = (sumDelay / 50) + " " + i;

            try (FileWriter fw = new FileWriter("graph1.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(data1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String data2 = (sumQueueSize / 50) + " " + i;

            try (FileWriter fw = new FileWriter("graph2.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(data2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < sourceNum; j++) {
                lambdas[j] = 0.5 + j * 0.1;
            }
            double avgDelaySources[] = new double[sourceNum];
            double avgPacketDrop[] = new double[sourceNum];
            calc(sourceNum, lambdas, packetLength, false, true, false, avgDelaySources, avgPacketDrop);
            for (int j = 0; j < sourceNum; j++) {
                lambdas[j] = 0.1 + j * 2;
            }
            calc(sourceNum, lambdas, packetLength, false, true, true, avgDelaySources, avgPacketDrop);

            StringBuilder data = new StringBuilder();

            for (int k = 0; k < sourceNum; k++) {
                data.append(avgDelaySources[k]).append(" ");
            }

            try (FileWriter fw = new FileWriter("graph3.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder data2 = new StringBuilder();

            for (int k = 0; k < sourceNum; k++) {
                data2.append(avgPacketDrop[k]).append(" ");
            }

            try (FileWriter fw = new FileWriter("graph4.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(data2);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

//        String[] cmdScript = new String[]{"/bin/bash", "gnuplot.sh"};
//        try {
//            Runtime.getRuntime().exec(cmdScript);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            Runtime.getRuntime().exec("python3 Assignment2.py");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static double calc(int sourceNum, double lambdas[], int packetLength,
                               boolean calcDelay, boolean diffLambda, boolean pktLoss,
                               double avgDelaySources[], double avgPacketDrop[]) {

        boolean lastOut = false;
        double lastOutTime = 0;

        double totalDelay = 0, bs = 20, bssT = 100;

        double qEntered[] = new double[sourceNum];
        double qReached[] = new double[sourceNum];

        int sourceId = 0, currentQueueSize = 0, queueLength = 25,
                packetEnteredQueue = 0, packetReachedSink = 0, sumQueueSize = 0;

        HashMap<Integer, Link> linkHashMap = new HashMap<>();

        linkHashMap.put(11, new Link(11, bs));
        linkHashMap.put(12, new Link(12, bssT));

        HashMap<Integer, Switch> switchHashMap = new HashMap<>();
        PriorityQueue<Event> switchPriorityQueue = new PriorityQueue<>();


        switchHashMap.put(1001, new Switch(1001, switchPriorityQueue, 12));


        HashMap<Integer, Source> sourceHashMap = new HashMap<>();

        int packet_Id = 123;

        HashMap<Integer, Packet> packetHashMap = new HashMap<>();


        PriorityQueue<Event> eventPriorityQueue = new PriorityQueue<>();


        for (int i = 0; i < sourceNum; i++) {

            double randomNum = ThreadLocalRandom.current().nextDouble(0, 1);

            if (diffLambda) {

                sourceHashMap.put(sourceId, new Source(sourceId, lambdas[i], 11, 1001));
            } else {

                sourceHashMap.put(sourceId, new Source(sourceId, lambdas[0], 11, 1001));
            }
            packetHashMap.put(packet_Id, new Packet(packet_Id, packetLength, randomNum, sourceId++, 0));
            eventPriorityQueue.add(new Event(0, randomNum, packet_Id++));

        }


        outer:
        while (true) {
            Event event = eventPriorityQueue.poll();
            Packet packet = packetHashMap.get(event.getPacketId());
            Source source = sourceHashMap.get(packet.getSourceId());


            switch (event.getType()) {

                case 0: {
                    double random = Math.random();

                    if (random == 1)
                        random -= 0.01;
                    double t = -Math.log(1 - random) / source.getPacketGenerationRate();

                    // Creating new packet
                    packetHashMap.put(packet_Id,
                            source.generatePacket(packet_Id,
                                    packet.getPacketLength(),
                                    packet.getCreationTimestamp() + t));

                    // Creating event E0
                    eventPriorityQueue.add(
                            new Event(0, packet.getCreationTimestamp() + t, packet_Id++)
                    );

                    // Creating event E1
                    double l_bs = ((double) packet.getPacketLength()) / linkHashMap.get(source.getLinkId()).getBandwidth();

                    eventPriorityQueue.add(
                            new Event(1, packet.getCreationTimestamp() + l_bs, packet.getPacketID())
                    );

                    break;
                }

                case 1: {
                    qReached[source.getSourceId()]++;
                    sumQueueSize += currentQueueSize;

                    if (pktLoss && currentQueueSize >= queueLength) {
                        avgPacketDrop[source.getSourceId()] += 1;
                        break;
                    }

                    long n_l = currentQueueSize * packetHashMap.get(event.getPacketId()).getPacketLength();

                    double bss = linkHashMap.get(switchHashMap.get(source.getSwitchId()).getLinkId()).getBandwidth();
                    double n_l_bss = ((double) n_l) / bss;

                    double tx = 0;

                    if (lastOut) {
                        double tmp = (double) packetHashMap.get(event.getPacketId()).getPacketLength() / bss;
                        tx = tmp - (event.getTimestamp() - lastOutTime);
                        if (tx < 0) {
                            tx = 0;
                        }

                    }

                    eventPriorityQueue.add(
                            new Event(2, event.getTimestamp() + n_l_bss + tx, packet.getPacketID())
                    );

                    if (diffLambda && !pktLoss) {
                        avgDelaySources[source.getSourceId()] += n_l_bss + tx;
                        qEntered[source.getSourceId()]++;
                    } else
                        totalDelay += n_l_bss + tx;
                    currentQueueSize++;
                    packetEnteredQueue++;
                    break;

                }

                case 2: {
                    double bss = linkHashMap.get(switchHashMap.get(source.getSwitchId()).getLinkId()).getBandwidth();
                    double l_bss = (double) packetHashMap.get(event.getPacketId()).getPacketLength() / bss;

                    packet.setDeletiontimestamp(event.getTimestamp() + l_bss);

                    currentQueueSize--;
                    lastOut = true;
                    lastOutTime = event.getTimestamp();

                    eventPriorityQueue.add(
                            new Event(3, event.getTimestamp() + l_bss, packet.getPacketID())
                    );
                    break;

                }

                case 3: {
                    packetReachedSink++;

                    if (packetReachedSink == 1000)
                        break outer;
                    break;
                }

                default: {
                    System.out.println("It should not reach here");
                    break;
                }
            }

        }

        double averageDelay = (totalDelay / packetEnteredQueue);
        averageDelay += packetLength * (1.0 / bs + 1.0 / bssT);

        double averageQueueSize = (sumQueueSize / packetEnteredQueue);

        if (diffLambda && pktLoss) {
            for (int i = 0; i < sourceNum; i++) {
                avgPacketDrop[i] = (avgPacketDrop[i] / qReached[i]);
            }
        } else if (diffLambda && !pktLoss) {
            for (int i = 0; i < sourceNum; i++) {
                avgDelaySources[i] = (avgDelaySources[i] / qEntered[i]);
                avgDelaySources[i] += packetLength * (1.0 / bs + 1.0 / bssT);
            }
        }

        if (calcDelay && !diffLambda)
            return averageDelay;
        else if (!calcDelay && !diffLambda)
            return averageQueueSize;
        else
            return 0;

    }

}
