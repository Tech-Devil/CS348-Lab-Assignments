import java.util.Random;

public class FixFingers extends Thread{

    private Node node;
    private Random random;
    private boolean threadRunning;

    FixFingers(Node node){
        this.node = node;
        threadRunning = true;
        random = new Random();
    }

    @Override
    public void run() {
        while (threadRunning){
            int i = random.nextInt(4)+1;
            String ithfinger = node.find_successor(i);
            node.updateIthFinger(i, ithfinger);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        this.threadRunning = threadRunning;
    }
}
