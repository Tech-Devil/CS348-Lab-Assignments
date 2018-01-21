public class Event implements Comparable<Event>{

    private int type;
    private double timestamp;
    private int packetId;

    Event(int type, double timestamp, int packetId) {
        this.type = type;
        this.timestamp = timestamp;
        this.packetId = packetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    @Override
    public int compareTo(Event event) {
        if (timestamp > event.timestamp)
            return 1;
        else if (timestamp < event.timestamp)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Type = "+type+" timestamp = "+timestamp+" packetId = "+packetId;
    }
}
