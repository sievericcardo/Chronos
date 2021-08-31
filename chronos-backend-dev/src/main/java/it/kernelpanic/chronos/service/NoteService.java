package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.NoteDao;
import it.kernelpanic.chronos.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service class for the notes that the various users will create and handle.
 */
@Component
public class NoteService {

    @Autowired
    NoteDao repo;

    /**
     * Service that create a new record in the note table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param note object to be added
     * @return true if it succeeded
     */
    public long add(Note note) {
        return repo.create(note);
    }

    /**
     * Delete a specific note
     *
     * @param id identifier for the note
     * @return true if it succeeded
     */
    public boolean delete(Long uid, Long id) {
        return repo.delete(uid, id);
    }

    /**
     * Retrieve a single note from the database
     *
     * @param id identifier of the note
     * @return the note corresponding
     */
    public Note getNoteById(Long id) {
        return repo.getNoteById(id);
    }

    /**
     * Retrieve a list of note for a specific user
     *
     * @param user_id identifier of the user
     * @return the list of notes corresponding
     */
    public List<Note> getAllNotesByUserId(long user_id) {
        return repo.getAllNotesByUserId(user_id);
    }

    /**
     * Update a specific note
     *
     * @param note object to be update
     * @return true if it succeeded
     */
    public boolean update(Note note) {
        return repo.update(note);
    }
}
