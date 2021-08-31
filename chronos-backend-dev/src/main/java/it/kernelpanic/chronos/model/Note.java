package it.kernelpanic.chronos.model;

/**
 * Note model. Every user has its own space in which he can create and edit
 * notes.
 * As a feature, we also allowed the user to choose a colour for the note to
 * make it easier for him to index.
 */
public class Note {

    private long id;
    private String body;
    private long created_by;
    private long created_at;
    private int color;

    public Note() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                ", color=" + color +
                '}';
    }
}
