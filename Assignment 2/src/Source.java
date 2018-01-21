public class Source {

    private int sourceId;
    private double packetGenerationRate;
    private int linkId;
    private int switchId;

    Source(int sourceId, double packetGenerationRate, int linkId, int switchId) {
        this.sourceId = sourceId;
        this.packetGenerationRate = packetGenerationRate;
        this.linkId = linkId;
        this.switchId = switchId;
    }

    public Packet generatePacket(int packetID, int packetLength, double creationTimestamp) {
        return new Packet(packetID, packetLength, creationTimestamp, sourceId, 0);
    }


    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public double getPacketGenerationRate() {
        return packetGenerationRate;
    }

    public void setPacketGenerationRate(double packetGenerationRate) {
        this.packetGenerationRate = packetGenerationRate;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }
}
