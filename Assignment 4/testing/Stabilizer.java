
public class Stabilizer extends Thread {

    private Node node;
    private boolean threadRunning;

    Stabilizer(Node node) {
        this.node = node;
        threadRunning = true;
    }

    @Override
    public void run() {
        while (threadRunning) {

            String successor = node.getSuccessor();
            if (successor == null || successor.equals(node.getLocalAddress()))
                node.fillSuccessor();
            successor = node.getSuccessor();
            if (successor != null && !successor.equals(node.getLocalAddress())) {
                String x = Helper.sendReq(successor, "Your_predecessor?");
                if (x == null) {
                    node.deleteSuccessor();
                } else if (!x.equals(successor)) {
                    int localId = node.getId();
                    int successorId = Helper.hashIt(successor);
                    successorId = localId>successorId?successorId+32:successorId;
                    int xId = Helper.hashIt(x);
                    xId = localId>xId?xId+32:xId;
                    if (xId > localId && xId < successorId) {
                        node.updateIthFinger(0, x);
                    }
                } else {
                    node.notify(successor);
                }
            }

            try {
                Thread.sleep(100);
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
