package it.kernelpanic.chronos.model;

import java.io.Serializable;

/**
 * Model for the EventMessage. Users can communicate by posting messages
 * regarding specific events; as such, we need to store information about
 * the user posting the message, as well as the event it corresponds to.
 */
public class EventMessage implements Serializable {
    private String body;
    private long created_by;
    private long event_id;
    private long created_at;
    private String user_avatar;
    private String username;

    public EventMessage() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
