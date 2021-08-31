package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Tag;

import javax.sql.DataSource;

/**
 * Interface for the Tag table. We allow users to use tags to better index
 * events creating a classification.
 */
public interface TagDao {
    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    /**
     * Create a record of Tag in the corresponding table
     *
     * @param tag object of type tag to be inserted in the database
     * @return true if it succeeded
     */
    boolean create(Tag tag);

    /**
     * Retrieve a single tag given an id
     *
     * @param id identifier for the tag retrieval
     * @return the corresponding tag
     */
    Tag getTagNameById(Long id);

    /**
     * Delete a specific tag from the table
     *
     * @param id tag identifier
     * @return true if it succeeded
     */
    boolean delete(Long id);

    void cleanup();
}
