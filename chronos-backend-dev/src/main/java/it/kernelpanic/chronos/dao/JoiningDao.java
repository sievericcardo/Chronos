package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.EventResponse;
import it.kernelpanic.chronos.model.Joining;
import it.kernelpanic.chronos.model.User;

import javax.sql.DataSource;
import java.util.List;

/**
 * Interface for the Joining table containing the signatures of the methods
 * to be implemented.
 * We define joinings in terms of users that participate to specific events, 
 * having the event and the user as key for the single object.
 */
public interface JoiningDao {
    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    /**
     * Create a new joining record in the corresponding table
     *
     * @param event_id identifier of the event to which a user participate
     * @param user_id identifier of the user that participates
     * @return true if it succeeded
     */
    boolean create(long event_id, long user_id);

    /**
     * Get the list of all joining of events for a specific user
     *
     * @param id identifier of user
     * @return the list of joinings of events corresponding
     */
    List<EventResponse> getAllJoiningsByUserId(long id);

    /**
     * Get the list of all joining of events for a specific event
     *
     * @param id identifier of event
     * @return the list of joinings of users corresponding
     */
    List<User> getAllJoiningsByEventId(long id);

    // Update an existing joining
    boolean update(Joining join);

    /**
     * Remove a specific record from the table
     *
     * @param event_id event identifier for the joining
     * @param user_id user identifier for the joining
     * @return true if it succeeded
     */
    boolean delete(long user_id, long event_id);

    void cleanup();
}
