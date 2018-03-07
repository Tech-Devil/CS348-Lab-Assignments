import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    private Node node;
    private ServerSocket serverSocket;
    private boolean threadRunning;

    Listener(Node node){
        this.node = node;
        threadRunning = true;

        try {
            serverSocket = new ServerSocket(Integer.parseInt(node.getLocalAddress().split(":")[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (threadRunning){
            Socket socket = null;

            try {
//                System.out.println("waiting for socket to get accept -----66666666666666666");
                socket = serverSocket.accept();
//                System.out.println("socket accepted -----66666666666666666");
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread(new Chat(socket, node)).start();
        }
    }


    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        this.threadRunning = threadRunning;
    }
}
