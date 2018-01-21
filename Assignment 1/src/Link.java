public class Link {

    private int linkId;
    private double bandwidth;

    Link(int linkId, double bandwidth) {
        this.linkId = linkId;
        this.bandwidth = bandwidth;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }
}
