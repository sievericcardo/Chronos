package it.kernelpanic.chronos.model;

/**
 * Model for the EventResponse. Users can communicate by posting and answering messages
 * regarding specific events; as such, we need to store information about the user and its information
 * posting the message, the event it corresponds to, including the position and the description.
 */
public class EventResponse {

    private long event_id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private long created_by;
    private String location;
    private String landing_image;
    private long created_at;
    private long event_when;
    private String user_avatar;
    private String username;

    public EventResponse() {
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanding_image() {
        return landing_image;
    }

    public void setLanding_image(String landing_image) {
        this.landing_image = landing_image;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getEvent_when() {
        return event_when;
    }

    public void setEvent_when(long event_when) {
        this.event_when = event_when;
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
