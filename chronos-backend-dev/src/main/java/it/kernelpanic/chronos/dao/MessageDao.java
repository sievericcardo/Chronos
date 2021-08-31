package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.EventMessage;
import it.kernelpanic.chronos.model.Message;

import javax.sql.DataSource;
import java.util.List;

/**
 * Interface for the Message table. As part of the application concept, users
 * can post messages to communicate with other participants making the whole
 * ecosystem more "social".
 * This interface contains the signatures for the CRUD operations of the messages.
 */
public interface MessageDao {

    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    /**
     * Create a record in the corresponding table
     *
     * @param msg object of type message that will be inserted in the table
     * @return true if it succeeded
     */
    boolean create(Message msg);

    /**
     * Retrieve the message record from a specific id
     *
     * @param id identifier for the message that need to be retrieved
     * @return the message
     */
    Message getMessageById(Long id);

    /**
     * Retrieve all the records from the message table from a specific event id
     *
     * @param id event identifier
     * @return the list of event message
     */
    List<EventMessage> getAllMessagesByEventId(Long id);

    /**
     * Delete a specific message from the table
     *
     * @param id message identifier
     * @return true if it succeeded
     */
    boolean delete(Long id);

    /**
     * Update a message record
     *
     * @param msg object that will be updated
     * @return true if it succeeded
     */
    boolean update(Message msg);

    void cleanup();
}
