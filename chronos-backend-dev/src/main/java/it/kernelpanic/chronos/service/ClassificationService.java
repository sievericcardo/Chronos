package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.ClassificationDao;
import it.kernelpanic.chronos.model.Classification;
import it.kernelpanic.chronos.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service for the classification objects. Every operations done on classifications
 * requires the service to generate the appropriate models. The latter will be used
 * in conjunction with the dao for the queries.
 */
@Component
public class ClassificationService {

    @Autowired
    ClassificationDao classificationDao;

    /**
     * Service that create a new record in the classification table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param classification object to be inserted
     * @return true if it succeeded
     */
    boolean create(Classification classification) {
        return classificationDao.create(classification);
    }

    /**
     * Retrieve the list of tags of a specific event
     *
     * @param id identifier for the event
     * @return the list of tag corresponding to that event
     */
    List<Tag> getAllTagsByEventId(Long id) {
        return classificationDao.getAllTagsByEventId(id);
    }

    /**
     * Delete a specific couple of tag and event
     *
     * @param tag_id identifier for the tag
     * @param event_id identifier for the event
     * @return true if it succeeded
     */
    boolean delete(Long tag_id, Long event_id) {
        return classificationDao.delete(tag_id, event_id);
    }
}
