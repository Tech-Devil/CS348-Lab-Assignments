import java.io.*;
import java.net.Socket;

public class TCPClient {

    public static void main(String argv[]) throws IOException {

        String sentence;
        String modifiedSentence;

//        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 1234);

//        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//
//        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//        sentence = inFromUser.readLine();
//
//        outToServer.writeBytes(sentence + '\n');
//
//        modifiedSentence = inFromServer.readLine();
//
//        System.out.println("FROM SERVER: " + modifiedSentence);


        FileOutputStream fos = new FileOutputStream("test.html");
        BufferedOutputStream out = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024];
        int count;
        InputStream in = clientSocket.getInputStream();

        System.out.println("Into the loop");
        while((count=in.read(buffer)) >= 0){
            fos.write(buffer, 0, count);
        }

        System.out.println("Out of the loop");
        fos.close();


        clientSocket.close();

    }
}
