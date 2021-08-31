package it.kernelpanic.chronos.model;

/**
 * Classification model. We create classification objects based on the event
 * they correspond to (event id) and the corresponding tag (tag id). Every event
 * can have more than one tag.
 */
public class Classification {
    private long event_id;
    private long tag_id;

    public Classification() {
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "Classification{" +
                "event_id=" + event_id +
                ", tag_id=" + tag_id +
                '}';
    }
}
