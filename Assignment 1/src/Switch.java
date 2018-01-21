import java.util.PriorityQueue;

public class Switch {

    private int switchId;
    private PriorityQueue<Event> eventPriorityQueue = new PriorityQueue<>();
    private int linkId;

    Switch(int switchId, PriorityQueue<Event> eventPriorityQueue, int linkId) {
        this.switchId = switchId;
        this.eventPriorityQueue = eventPriorityQueue;
        this.linkId = linkId;
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }

    public PriorityQueue<Event> getEventPriorityQueue() {
        return eventPriorityQueue;
    }

    public void setEventPriorityQueue(PriorityQueue<Event> eventPriorityQueue) {
        this.eventPriorityQueue = eventPriorityQueue;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }
}
