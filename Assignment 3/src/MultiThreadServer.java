import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

public class MultiThreadServer implements Runnable {

    private Socket socket;
    private static int maxReq, count;
    private static LinkedList<String> inetAddressesToBlock = new LinkedList<>();
    private static String defaultFile;

    private MultiThreadServer(Socket socket) {
        this.socket = socket;
    }

    public static void main(String args[]) throws Exception {

        if (loadConfig()) {

            ServerSocket ssock = new ServerSocket(1234);

            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.getHostAddress().startsWith("172.16."))
                        System.out.print("Server started listening at -----> " + i.getHostAddress() + ":" + 1234);
                }
            }


            while (true) {

                Socket sock = ssock.accept();

                if (!inetAddressesToBlock.contains(sock.getInetAddress().toString()) && count < maxReq) {
                    count++;
                    System.out.println("Count = " + count);
                    new Thread(new MultiThreadServer(sock)).start();
                } else
                    sock.close();
            }
        }

    }


    @Override
    public void run() {

        BufferedReader inFromClient = null;
        try {
            inFromClient = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));

            if (!inFromClient.ready()) {
                socket.close();
                inFromClient.close();
                return;
            }

            String clientSentence = inFromClient.readLine();
            if (clientSentence == null) {
                socket.close();
                inFromClient.close();
                System.out.println("Null string");
            } else {

                System.out.println("Client request -- > " + clientSentence);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                File folder = new File(".");
                File[] listOfFiles = folder.listFiles();

                Boolean found = false;

                for (File file : listOfFiles) {

                    if (file.isFile()) {
                        if (file.getName().equals(clientSentence.split("/")[1].split(" ")[0])) {

                            out.println("HTTP/1.1 200 OK\r");
                            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpg"))
                                out.println("Content-Type: image/jpeg\r");
                            else if (file.getName().endsWith(".gif"))
                                out.println("Content-Type: image/gif\r");
                            else if (file.getName().endsWith(".html") || file.getName().endsWith(".htm"))
                                out.println("Content-Type: text/html\r");
                            out.println("Content-Length: " + file.length() + "\r");
                            out.println("\r");
                            int count;
                            byte[] buffer = new byte[1024];
                            OutputStream output = socket.getOutputStream();
                            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                            while ((count = in.read(buffer)) >= 0) {
                                output.write(buffer, 0, count);
                                output.flush();
                            }


                            out.close();
                            output.close();
                            in.close();
                            found = true;
                            break;

                        }
                    }
                }

                if (!found) {

                    File errFile = new File("404Error.html");
                    out.println("HTTP/1.1 404 Not Found\r");
                    out.println("Content-Type: text/html\r");
                    out.println("Content-Length: " + errFile.length() + "\r");
                    out.println("\r");
                    int count;
                    byte[] buffer = new byte[1024];
                    OutputStream output = socket.getOutputStream();
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(errFile));
                    while ((count = in.read(buffer)) >= 0) {
                        output.write(buffer, 0, count);
                        output.flush();
                    }
                    out.close();
                    output.close();
                    in.close();
                }

                socket.close();

                System.out.println("Count before sleep = " + count);
                Thread.sleep(20000);
                count--;
                System.out.println("Count after sleep = " + count);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static boolean loadConfig() {


        Properties prop = new Properties();
        InputStream input = null;

        try {

            File file = new File("config.properties");
            if (file.exists()) {

                input = new FileInputStream(file);
                prop.load(input);

                maxReq = Integer.parseInt(prop.getProperty("max_con_requests"));
                Collections.addAll(inetAddressesToBlock, prop.getProperty("blocked_ips").split(","));
                defaultFile = prop.getProperty("default_file");

                return true;
            } else {
                System.out.println("Config file not present!");
                return false;
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }

        }


        return false;
    }

}

