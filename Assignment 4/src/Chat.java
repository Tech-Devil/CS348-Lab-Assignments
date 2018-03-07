import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Chat implements Runnable {

    private Socket socket;
    private Node node;

    public Chat(Socket socket, Node node) {
        this.socket = socket;
        this.node = node;
//        System.out.println("Here");
    }


    @Override
    public void run() {

//        System.out.println("Now here");

        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            String request = Helper.inputStreamToString(inputStream);

//            System.out.println("chat request recieved");
            String response = response(request);
            if (response != null) {
                output.write(response.getBytes());
//                System.out.println("chat response send");
            }
//            System.out.println("chat response not send");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String response(String request) {

        if (request == null)
            return null;

        String response = null;

        if (request.startsWith("Find_successor-")) {
            int id = Integer.parseInt(request.split("-")[1]);
            response = node.find_successor(id);

        } else if (request.startsWith("Present?")) {
            response = "Yes";

        } else if (request.startsWith("Your_successor?")) {
            response = node.getSuccessor();
            if (response == null) {
                response = "None";
            }
        } else if (request.startsWith("Closest_finger-")) {
            int id = Integer.parseInt(request.split("-")[1]);
            response = node.closest_preceding_finger(id);
        } else if (request.startsWith("I_am_your_pre-")) {

            String predecessor = request.split("-")[1];
            node.notified(predecessor);

            File[] files = new File("files").listFiles();

            int predecessorId = Helper.hashIt(predecessor);
            int add = node.getId() > predecessorId ? 0 : 32;

            if (files == null) {
                System.out.println("No files found");
            } else {

                int len = files.length, fileId;
                boolean firstFile = true;
                for (int i = 0; i < len; i++) {
//                    if (i==0)
//                        response=files[0].getName()+" ";
//                    else
//                        response+=files[i].getName()+" ";

                    fileId = Helper.hashIt(files[i].getName());
                    fileId = fileId > node.getId() ? fileId : fileId + add;

                    if (fileId >= predecessorId && fileId < (node.getId() + add)) {

                        if (firstFile) {
                            response = files[i].getName() + " ";
                            firstFile = false;
                        } else {
                            response += files[i].getName() + " ";
                        }
                    }
                }
            }

        } else if (request.startsWith("Your_predecessor?")) {
            response = node.getPredecessor();
            if (response == null)
                response = "None";
        }

        return response;

    }


}
