public class CheckPredecessor extends Thread {

    private Node node;
    private boolean threadRunning;

    CheckPredecessor(Node node) {
        this.node = node;
        threadRunning = true;
    }

    @Override
    public void run() {
        while (threadRunning) {
            String predecessor = node.getPredecessor();
            if (predecessor != null) {
                String response = Helper.sendReq(predecessor, "Present?");
                if (response == null || !response.equals("Yes")) {
                    node.setPredecessor(null);
                }

            }
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
