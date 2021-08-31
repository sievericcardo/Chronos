package it.kernelpanic.chronos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.kernelpanic.chronos.model.*;
import it.kernelpanic.chronos.model.Event;
import it.kernelpanic.chronos.service.EventService;
import it.kernelpanic.chronos.service.JoiningService;
import it.kernelpanic.chronos.service.MessageService;
import it.kernelpanic.chronos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.Principal;
import java.util.List;

/**
 * Controller that maps the various events methods inside the rest api
 * pattern to create and retrieve new events.
 */
@RestController
@RequestMapping("/api/events")
@Api(value = "EventsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

    @Autowired
    EventService service;

    @Autowired
    UserService uservice;

    @Autowired
    MessageService mservice;

    @Autowired
    JoiningService jservice;

    /**
     * Retrieve all the events from the corresponding table
     *
     * @return the list of events from the database
     */
    @GetMapping("/all")
    @ApiOperation("Get all the events")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = List.class)})
    public List<EventResponse> getEvents() {
        return service.getAllEvents();
    }

    /**
     * Create a new event from the rest api server controller. The creation is handled by the service
     *
     * @param principal object that is used to retrieve the information required for creating a new event
     * @param evt event taken from the request body
     * @return true if it succeeded in creating the event
     */
    @PostMapping("/new")
    @ApiOperation("Create a new event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean postEvents(Principal principal, @RequestBody Event evt) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        evt.setCreated_by(uid);
        return service.add(evt);
    }

    /**
     * Create a new joining element in the table.
     *
     * @param principal object passed from which we get the name as a string
     * @param event_id event identifier get as a request parameter
     * @return true if it succeeded
     */
    @PostMapping("/join")
    @ApiOperation("Participate to an event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean postJoining(Principal principal, @RequestParam("event_id") String event_id) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);

        return jservice.create(Long.parseLong(event_id), uid);
    }

    /**
     * Create a new comment in the corresponding table.
     *
     * @param principal object from which we will retrieve the uid
     * @param message request body message for the comment
     * @return true if it succeeded
     */
    @PostMapping("/comment")
    @ApiOperation("Post a new comment")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean postComment(Principal principal, @RequestBody Message message) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        message.setCreated_by(uid);

        return mservice.add(message);
    }

    /**
     * Get all comments for a specific event
     *
     * @param event_id identifier for the event get from the request parameter
     * @return the list of all the EventMessages
     */
    @GetMapping("/comments/all")
    @ApiOperation("Get all the comments")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = List.class)})
    public List<EventMessage> getAllCommentsByEventId(@RequestParam("event_id") String event_id) {
        return mservice.getAllCommentsByEventId(Long.parseLong(event_id));
    }

    /**
     * Get the event from its id.
     *
     * @param event_id event id requested as parameter
     * @return the event corresponding to the id
     */
    @GetMapping("/event")
    @ApiOperation("Get a specific event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = EventResponse.class)})
    public EventResponse getById(@RequestParam("event_id") String event_id) {
        return service.getEventById(Long.parseLong(event_id));
    }

    /**
     * Get the list of type user corresponding to the joinings of a specific event
     *
     * @param event_id event identifier requested as parameter
     * @return the list of joinings corresponding to that specific event
     */
    @GetMapping("/get/user")
    @ApiOperation("Get all the events for a specific user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = List.class)})
    public List<User> getAllJoiningsByEventId(@RequestParam("event_id") String event_id) {
        return jservice.getAllJoiningsByEventId(Long.parseLong(event_id));
    }

    /**
     * Get all the joinings provided the user
     *
     * @param principal object from which we get the user identifier
     * @return the list oj joinings corresponding to the specific user
     */
    @GetMapping("/myjoinings")
    @ApiOperation("Get all the participations for a specific user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = List.class)})
    public List<EventResponse> getAllJoiningsByUserId(Principal principal) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        return jservice.getAllJoiningsByUserId(uid);
    }

    /**
     * Remove a participation record
     *
     * @param principal object for the user identifier
     * @param event_id event identifier
     * @return true if it succeeded
     */
    @DeleteMapping("/unjoin")
    @ApiOperation("Remove a participation for a user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean deleteJoining(Principal principal, @RequestParam("event_id") String event_id) {
        String x = principal.getName();
        long uid = uservice.getUserIdByUsername(x);
        return jservice.deleteJoining(uid, Long.parseLong(event_id));
    }

    /**
     * Delete a specific event based on its id
     *
     * @param id event identifier to be removed
     * @return true if it succeeded
     */
    @DeleteMapping("/del/{id}")
    @ApiOperation("Remove a specific event")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean delete(@PathVariable() long id) {
        return service.delete(id);
    }
}

