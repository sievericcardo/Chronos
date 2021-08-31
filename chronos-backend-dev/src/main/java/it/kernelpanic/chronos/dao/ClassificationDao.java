package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Classification;
import it.kernelpanic.chronos.model.Tag;

import javax.sql.DataSource;
import java.util.List;

/**
 * Interface for the Classification. We define the signatures of the methods that
 * will have to be implemented by the classes that will use this interface.
 * The classification table requires methods for creating, selecting and deleting
 * data from the table.
 */
public interface ClassificationDao {
    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    /**
     * Create a record in the classification table
     *
     * @param classification classification object
     * @return true if it succeeded
     */
    boolean create(Classification classification);

    /**
     * Retrieve all classification from a specific event
     *
     * @param id event identifier
     * @return the list of all classification
     */
    List<Tag> getAllTagsByEventId(Long id);

    // Delete a specific tag_id,event_id couple.
    boolean delete(Long tag_id, Long event_id);

    void cleanup();
}
