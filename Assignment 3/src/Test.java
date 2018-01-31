import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;

public class Test {

    public static void main(String args[]) throws UnknownHostException, SocketException {

//        File folder = new File(".");
//        File[] listOfFiles = folder.listFiles();
//
//
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                System.out.println("File " + listOfFiles[i].getName());
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }


        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: "+addr.getHostAddress());
        String hostname = addr.getHostName();
        System.out.println("Local host name: "+hostname);


        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.getHostAddress().startsWith("172.16."))
                    System.out.print("----------> ");
                System.out.println(i.getHostAddress());
            }
        }

        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        try {

//            output = new FileOutputStream("config.properties");
//
//            // set the properties value
//            prop.setProperty("database", "localhost");
//            prop.setProperty("dbuser", "mkyong");
//            prop.setProperty("dbpassword", "password");
//
//            // save properties to project root folder
//            prop.store(output, null);


//            input = new FileInputStream("config.properties");
            File file = new File("config.properties");

            if (file.exists()) {

                input = new FileInputStream(file);
                // load a properties file
                prop.load(input);

                // get the property value and print it out
                System.out.println(prop.getProperty("database"));
                System.out.println(prop.getProperty("dbuser"));
                System.out.println(prop.getProperty("dbpassword").split(",")[0]);
                System.out.println(prop.getProperty("dbpassword").split(",")[1]);
                System.out.println(prop.getProperty("dbpassword").split(",")[2]);
            } else
                System.out.println("File not found");
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
//            if (output != null) {
//                try {
//                    output.close();
//                } catch (IOException err) {
//                    err.printStackTrace();
//                }
//            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }

        }



    }



}
