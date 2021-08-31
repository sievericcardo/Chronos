package it.kernelpanic.chronos.model;

import java.util.Objects;

/**
 * Event models. Every event will have an id as primary key. We store name and
 * description for every event, as well as the due date, the place (using
 * coordinates), an image and the user that created it.
 */
public class Event {

    private long id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private long created_by;
    private String location;
    private String landing_image;
    private long created_at;
    private long event_when;

    public Event() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getEvent_when() {
        return event_when;
    }

    public void setEvent_when(long date) {
        this.event_when = date;
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

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getCreated_by(), getLocation());
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", created_by=" + created_by +
                ", location='" + location + '\'' +
                ", image='" + landing_image + '\'' +
                ", created_at=" + created_at +
                ", date=" + event_when +
                '}';
    }
}
