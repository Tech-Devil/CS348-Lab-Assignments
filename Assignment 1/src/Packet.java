public class Packet {

    private int packetID;
    private int packetLength;
    private double creationTimestamp;
    private int sourceId;
    private double deletiontimestamp;

    Packet(int packetID, int packetLength, double creationTimestamp, int sourceId, double deletiontimestamp) {
        this.packetID = packetID;
        this.packetLength = packetLength;
        this.creationTimestamp = creationTimestamp;
        this.sourceId = sourceId;
        this.deletiontimestamp = deletiontimestamp;
    }

    public int getPacketID() {
        return packetID;
    }

    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    public double getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(double creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public double getDeletiontimestamp() {
        return deletiontimestamp;
    }

    public void setDeletiontimestamp(double deletiontimestamp) {
        this.deletiontimestamp = deletiontimestamp;
    }

    @Override
    public String toString() {
        return "PacketId = "+packetID+" creationTimestamp = "+creationTimestamp+" sourceId = "+sourceId+" deletiontimestamp "+deletiontimestamp;
    }
}
