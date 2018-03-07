import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Objects;

public class Helper {

    public Helper() {

    }

    public static int hashIt(String s) {

        int n = 0;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            BitSet bitset = BitSet.valueOf(hash);

            for (int i = 0; i < 5; ++i) {
                n += (bitset.get(i) ? 1 : 0) * Math.pow(2, i);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return n;
    }


    public static String sendReq(String ip_port, String request) {

        if (ip_port == null || request == null)
            return null;

        String response;

        try {
            Socket socket = new Socket(ip_port.split(":")[0],
                    Integer.parseInt(ip_port.split(":")[1]));
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println(request);

            InputStream inputStream = socket.getInputStream();

//            System.out.println("request sent, waiting for response");
            response = inputStreamToString(inputStream);
//            System.out.println("response recieved");


            printStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }


    public static String inputStreamToString(InputStream in) {

        if (in == null) {
            return null;
        }
//        System.out.println("ab yaha");

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("Ho gaya");

        return line;
    }


//    public static void sendFiles(String address){
//
////        File folder = new File("files");
////        int count = Objects.requireNonNull(folder.listFiles()).length;
//
//        File[] files = new File("files").listFiles();
//
//        if (files==null){
//            System.out.println("This should not happen, no files found");
//            return;
//        }
//
//        Socket socket = null;
//        try {
//            socket = new Socket(address.split(":")[0], Integer.parseInt(address.split(":")[0]));
//
//            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
//            DataOutputStream dos = new DataOutputStream(bos);
//
//
//            dos.writeInt(files.length);
//            dos.write
//
//            for(File file : files)
//            {
//                long length = file.length();
//                dos.writeLong(length);
//
//                String name = file.getName();
//                dos.writeUTF(name);
//
//                FileInputStream fis = new FileInputStream(file);
//                BufferedInputStream bis = new BufferedInputStream(fis);
//
//                int theByte = 0;
//                while((theByte = bis.read()) != -1) bos.write(theByte);
//
//                bis.close();
//            }
//
//            dos.close();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}
