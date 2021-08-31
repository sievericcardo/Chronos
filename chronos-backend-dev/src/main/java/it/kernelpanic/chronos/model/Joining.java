package it.kernelpanic.chronos.model;

/**
 * Model for the joining object. We generate objects to map the single user
 * to the corresponding model. Note that an event may have multiple users; in 
 * that case, we will have multiple objects.
 */
public class Joining {
    private long event_id;
    private long user_id;

    public Joining(long event_id, long user_id) {
        this.event_id = event_id;
        this.user_id = user_id;
    }

    public Joining() {
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
