package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.EventDao;
import it.kernelpanic.chronos.model.Event;
import it.kernelpanic.chronos.model.EventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service for the events. We generates the single objects that are used in the
 * dao for the various queries.
 */
@Component
public class EventService {

    @Autowired
    EventDao repo;

    /**
     * Service that create a new record in the event table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param evt object to be inserted
     * @return true if it succeeded
     */
    public boolean add(Event evt) {
        return repo.create(evt);
    }

    /**
     * Delete a specific event
     *
     * @param id identifier for the event
     * @return true if it succeeded
     */
    public boolean delete(Long id) {
        return repo.delete(id);
    }

    /**
     * Retrieve the EventResponse
     *
     * @param id identifier for the event
     * @return the EventResponse corresponding
     */
    public EventResponse getEventById(long id) {
        return repo.getEventById(id);
    }

    /**
     * Retrieve all events
     *
     * @return the list of all events
     */
    public List<EventResponse> getAllEvents() {
        return repo.getAllEvents();
    }

    // TODO: add update feature
}
