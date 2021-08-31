package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.TagDao;
import it.kernelpanic.chronos.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service component for the various tags.
 * User can add and delete tags, as well as get the list of all tags of a
 * classification.
 */
@Component
public class TagService {

    @Autowired
    private TagDao tagDao;

    /**
     * Service that create a new record in the tag table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param tag object to be added
     * @return true if it succeeded
     */
    public boolean create(Tag tag) {
        return tagDao.create(tag);
    }

    /**
     * Retrieve a single tag record from the table
     *
     * @param id identifier to be found
     * @return the tag object corresponding
     */
    public Tag getTagNameById(Long id) {
        return tagDao.getTagNameById(id);
    }

    /**
     * Delete a specific tag from the table
     *
     * @param id identifier to be delete
     * @return true if it succeeded
     */
    public boolean delete(Long id) {
        return tagDao.delete(id);
    }
}
