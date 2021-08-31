package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Note;

import javax.sql.DataSource;
import java.util.List;

/**
 * Interface for the Note table. Users can create personal notes in a safe
 * manner.
 * In this interface we define the signature for the methods that have to be
 * implemented.
 */
public interface NoteDao {
    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    /**
     * Create a record in the event table
     *
     * @param note object note to be used for the insertion in the table
     * @return true if it succeeded
     */
    long create(Note note);

    /**
     * Retrieve the single note by a given event
     *
     * @param id identifier of the event to get the note
     * @return the corresponding note
     */
    Note getNoteById(Long id);

    /**
     * Retrieve the list of note for a specific user
     *
     * @param id user identifier
     * @return the list of notes for the given user id
     */
    List<Note> getAllNotesByUserId(Long id);

    /**
     * Delete a note for a specific user
     *
     * @param uid user identifier for the user check
     * @param id identifier of the note
     * @return true if it succeeded
     */
    boolean delete(Long uid, Long id);

    /**
     * Update a note for a specific user
     *
     * @param note object to be updated
     * @return true if it succeeded
     */
    boolean update(Note note);

    void cleanup();
}
