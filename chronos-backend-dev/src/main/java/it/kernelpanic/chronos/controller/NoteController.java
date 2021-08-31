package it.kernelpanic.chronos.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.kernelpanic.chronos.model.Note;
import it.kernelpanic.chronos.service.NoteService;
import it.kernelpanic.chronos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller used to map the note part inside the rest api server. Every communication
 * is done via url exposing the correct path for the CRUD operations on notes
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    NoteService service;

    @Autowired
    UserService uservice;

    /**
     * Create a new note for a specific user
     *
     * @param principal object from which we get the user id
     * @param note      parameter taken from the body. We parse it to a string
     * @return true if it succeeded
     */
    @PostMapping("/new")
    @ApiOperation("Create a new note")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public long postNotes(Principal principal, @RequestBody Note note) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        note.setCreated_by(uid);

        System.out.println(note.toString());

        return service.add(note);
    }

    /**
     * Update the specific note
     *
     * @param principal object for the user id
     * @param note parameter passed as request body
     * @return true if it succeeded
     */
    @PostMapping("/update")
    @ApiOperation("Update a specific note")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean updateNote(Principal principal, @RequestBody Note note) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        note.setCreated_by(uid);
        return service.update(note);
    }

    /**
     * Get all the notes from a specific user
     *
     * @param principal object for the user identifier
     * @return the list of notes for the given user
     */
    @GetMapping("/get")
    @ApiOperation("Get all the notes")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = List.class)})
    public List<Note> getAllNotesByUserId(Principal principal) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        return service.getAllNotesByUserId(uid);
    }

    /**
     * Every user can add or delete notes as part of the CRUD operations. We ensure that only the proprietary
     * user can delete his notes
     *
     * @param principal object for the user identifier
     * @param note_id   identifier element get from the path specified for the note
     * @return true if it succeeded
     */
    @DeleteMapping("/delete")
    @ApiOperation("Delete a specific note")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean delete(Principal principal, @RequestParam("note_id") String note_id) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        return service.delete(uid, Long.parseLong(note_id));
    }
}
