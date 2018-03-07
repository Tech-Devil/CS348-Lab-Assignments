import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Node {

    private String localAddress;
    //    private int port;
    private int id;
    private String[] fingerTable;
    private String predecessor;

    private Listener listener;
    private Stabilizer stabilizer;
    private FixFingers fixFingers;
    private CheckPredecessor checkPredecessor;

    Node(String localAddress) {

        this.localAddress = localAddress;
//        this.port = port;

        this.id = Helper.hashIt(localAddress);

        this.fingerTable = new String[5];
        predecessor = null;

        try {
            Runtime.getRuntime().exec("mkdir -p files");
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        listener = new Listener(this);
        stabilizer = new Stabilizer(this);
        fixFingers = new FixFingers(this);
        checkPredecessor = new CheckPredecessor(this);
    }

    public boolean join(String knownNode) {

        boolean join = true;

        if (knownNode != null && !knownNode.equals(localAddress)) {

            String successor = Helper.sendReq(knownNode, "Find_successor-" + id);
            if (successor == null) {
                System.out.println("No successor");
                join =  false;
                updateIthFinger(0, localAddress);
            }
            updateIthFinger(0, successor);
        } else if (knownNode != null && knownNode.equals(localAddress)) {
            updateIthFinger(0, localAddress);
            try {
                Runtime.getRuntime().exec("./run.sh");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listener.start();
        stabilizer.start();
        fixFingers.start();

        return join;
    }


    public String find_successor(int id) {
        String result = getSuccessor();

        String predecessor = find_predecessor(id);

        if (!predecessor.equals(localAddress))
            result = Helper.sendReq(predecessor, "Your_successor?");

        if (result == null)
            return localAddress;
        else if (result.equals("None"))     // TODO: 3/1/18 Can it be possible?
            return predecessor;

        return result;
    }


    private String find_predecessor(int id) {

        String n = localAddress;
        String n_successor = getSuccessor();
        String most_recently_alive = localAddress;

        id = this.id > id ? id + 32 : id;
        int n_successorId = id;
        if (n_successor != null) {
            n_successorId = Helper.hashIt(n_successor);
            n_successorId = this.id > n_successorId ? n_successorId + 32 : n_successorId;
        }

        while (!(id > this.id && id <= n_successorId)) {

            String pre_n = n;

            if (n.equals(localAddress))
                n = closest_preceding_finger(id);
            else {
                String result = Helper.sendReq(n, "Closest_finger-" + id);

                if (result == null) {
                    n = most_recently_alive;
                    n_successor = Helper.sendReq(n, "Your_successor?");
                    if (n_successor == null) {
                        System.out.println("System is completely imbalanced!!");
                        return localAddress;
                    }
                    continue;
                } else if (result.equals(n))
                    return result;
                else {
                    most_recently_alive = n;
                    n_successor = Helper.sendReq(result, "Your_successor?");
                    if (n_successor != null) {
                        n = result;
                    } else {
                        n_successor = Helper.sendReq(n, "Your_successor?");
                    }
                }

                // compute relative ids for while loop judgement
                n_successorId = Helper.hashIt(n_successor);
                n_successorId = Helper.hashIt(n) > n_successorId ? n_successorId + 32 : n_successorId;
                id = Helper.hashIt(n) > id ? id + 32 : id;
            }
            if (pre_n.equals(n))
                break;
        }
        return n;

    }


    public String closest_preceding_finger(int id) {

        id = this.id > id ? id + 32 : id;

        for (int i = 4; i >= 0; i--) {

            String ith_finger = fingerTable[i];
            if (ith_finger == null)
                continue;

            int ith_fingerId = Helper.hashIt(ith_finger);

            ith_fingerId = this.id > ith_fingerId ? ith_fingerId + 32 : ith_fingerId;

            if (ith_fingerId < id && ith_fingerId != this.id) {     // TODO: 3/1/18 Check if it is correct

                String response = Helper.sendReq(ith_finger, "Present?");

                if (response != null && response.equals("Yes"))
                    return ith_finger;
                else
                    deleteCertainFinger(ith_finger);

            }
        }
        return localAddress;
    }


    public String getSuccessor() {
        if (fingerTable != null)
            return fingerTable[0];
        return null;
    }


    private void deleteCertainFinger(String finger) {
        for (int i = 4; i >= 0; i--) {
            String ith_finger = fingerTable[i];
            if (ith_finger != null && ith_finger.equals(finger))
                fingerTable[i] = null;
        }
    }

    public synchronized void updateIthFinger(int pos, String value) {
        fingerTable[pos] = value;

        if (pos == 0 && value != null && !value.equals(localAddress))
            notify(value);

        if (fingerTable[0] == null)
            fingerTable[0] = localAddress;
    }

    public void notify(String successor) {

        String response = null;
        if (successor != null && !successor.equals(localAddress))
            response = Helper.sendReq(successor, "I_am_your_pre-" + localAddress);

        System.out.println(response);

        if (response!=null){
            String files[] = response.trim().split(" ");

//            System.out.println("\n\n\n\n\nResponse ---> ");
            for (int i = 0; i < files.length; i++) {
//                System.out.println(files[i]);

                try {
                    Runtime.getRuntime().exec("touch files/"+files[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notified(String predecessor) {
        if (this.predecessor == null || this.predecessor.equals(localAddress))
            this.predecessor = predecessor;
        else {
            int old_preId = Helper.hashIt(this.predecessor);
            int localId = old_preId > id ? id + 32 : id;
            int new_preId = Helper.hashIt(predecessor);
            new_preId = old_preId > new_preId ? new_preId + 32 : new_preId;
            if (new_preId > old_preId && new_preId < localId)
                setPredecessor(predecessor);
        }
    }

    public void fillSuccessor() {
        String successor = getSuccessor();
        if (successor == null || successor.equals(localAddress)) {
            for (int i = 1; i < 5; i++) {
                String ithfinger = fingerTable[i];
                if (ithfinger != null && !ithfinger.equals(localAddress)) {
                    for (int j = i - 1; j >= 1; j--) {
                        updateIthFinger(j, ithfinger);
                    }
                    break;
                }
            }
        }
        successor = getSuccessor();
        if ((successor == null || successor.equals(localAddress)) && predecessor != null && !predecessor.equals(localAddress)) {
            updateIthFinger(0, predecessor);
        }
    }


    public void deleteSuccessor() {
        String successor = getSuccessor();
        if (successor == null)
            return;
        int i;
        for (i = 4; i >= 0; i--) {
            String ithfinger = fingerTable[i];
            if (ithfinger != null && ithfinger.equals(successor))
                break;
        }
        for (int j = i; j >= 0; j--) {
            updateIthFinger(j, null);
        }
        if (predecessor != null && predecessor.equals(successor))
            setPredecessor(null);
        fillSuccessor();
        successor = getSuccessor();
        if ((successor == null || successor.equals(localAddress)) && predecessor != null && !predecessor.equals(localAddress)) {
            String p = predecessor;
            String p_pre = null;
            while (true) {
                p_pre = Helper.sendReq(p, "Your_predecessor?");
                if (p_pre == null)
                    break;
                if (p_pre.equals(p) || p_pre.equals(localAddress) || p_pre.equals(successor)) {
                    break;
                } else {
                    p = p_pre;
                }
            }
            updateIthFinger(0, p);
        }
    }


    public void stop() {

        if (listener != null)
            listener.setThreadRunning(false);
        if (stabilizer != null)
            stabilizer.setThreadRunning(false);
        if (fixFingers != null)
            fixFingers.setThreadRunning(false);
        if (checkPredecessor != null)
            checkPredecessor.setThreadRunning(false);
    }









    public String getLocalAddress() {
        return localAddress;
    }

    public synchronized void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public String[] getFingerTable() {
        return fingerTable;
    }

    public synchronized void setFingerTable(String[] fingerTable) {
        this.fingerTable = fingerTable;
    }

    public String getPredecessor() {
        return predecessor;
    }

    public synchronized void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }
}
