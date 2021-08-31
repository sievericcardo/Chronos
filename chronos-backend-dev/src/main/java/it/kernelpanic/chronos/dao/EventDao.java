package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Event;
import it.kernelpanic.chronos.model.EventResponse;
import it.kernelpanic.chronos.model.Tag;

import javax.sql.DataSource;
import java.util.List;

/**
 * Interface for the Event table. We define the signatures of the methods
 * that need to be implemented by the classes that makes use of this interface.
 * We define methods to create, select, delete and update the event table.
 */
public interface EventDao {

    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    /**
     * Create a new event record in the relative table
     *
     * @param event object that need to be used for the event record
     * @return true if it succeeded
     */
    boolean create(Event event);

    /**
     * Retrieve a single element for a given id
     *
     * @param id event identifier used to retrieve the event
     * @return the event corresponding to the id
     */
    EventResponse getEventById(Long id);

    /**
     * Retrieve all events
     *
     * @return the list of all events
     */
    List<EventResponse> getAllEvents();

    /**
     * Return the list of events near you
     *
     * @param latitude  used for the latitude coordinate
     * @param longitude used for the longitude coordinate
     * @return the list of events near you
     */
    List<EventResponse> getNearEvents(double latitude, double longitude);

    /**
     * Add a list of tags for an event
     *
     * @param list tags that need to be added for the event
     * @param event_id identifier of the event
     * @return true if it succeeded
     */
    boolean addTagListToEvent(List<Tag> list, Long event_id);

    /**
     * Delete an event in the database
     *
     * @param id event identifier
     * @return true if it succeeded
     */
    boolean delete(Long id);

    /**
     * Update the event record
     *
     * @param event object passed as element to get the id
     * @return true if succeeded
     */
    boolean update(Event event);

    void cleanup();
}
