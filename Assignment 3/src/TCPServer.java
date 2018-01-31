import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class TCPServer {

    public static void main(String argv[]) throws IOException {

        ServerSocket welcomeSocket = new ServerSocket(1234);

        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.getHostAddress().startsWith("172.16."))
                    System.out.print("Server started listening at -----> "+i.getHostAddress()+":"+1234);
            }
        }


        while (true) {

            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new
                    InputStreamReader(connectionSocket.getInputStream()));

            if (!inFromClient.ready()) {
                connectionSocket.close();
                inFromClient.close();
                continue;
            }

            String clientSentence = inFromClient.readLine();

            if (clientSentence == null) {
                connectionSocket.close();
                inFromClient.close();
                System.out.println("Null string");
            } else {

                System.out.println("Client request -- > " + clientSentence);
                PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);

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
                            OutputStream output = connectionSocket.getOutputStream();
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
                    OutputStream output = connectionSocket.getOutputStream();
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(errFile));
                    while ((count = in.read(buffer)) >= 0) {
                        output.write(buffer, 0, count);
                        output.flush();
                    }
                    out.close();
                    output.close();
                    in.close();
                }
                connectionSocket.close();
                inFromClient.close();
            }

        }

    }
}
