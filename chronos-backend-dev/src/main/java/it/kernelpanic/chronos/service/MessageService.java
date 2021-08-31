package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.MessageDao;
import it.kernelpanic.chronos.model.EventMessage;
import it.kernelpanic.chronos.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service class for the messages. Users can add and delete their own messages,
 * as well as read all messages in a specific event.
 */
@Component
public class MessageService {

    @Autowired
    MessageDao repo;

    /**
     * Service that create a new record in the message table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param msg object to be added
     * @return true if it succeeded
     */
    public boolean add(Message msg) {
        return repo.create(msg);
    }

    /**
     * Delete a specific message
     *
     * @param id identifier for the message
     * @return true if it succeeded
     */
    public boolean delete(Long id) {
        return repo.delete(id);
    }

    /**
     * Retrieve a specific message
     *
     * @param id identifier for the specific message
     * @return the corresponding message
     */
    public Message getMessageById(long id) {
        return repo.getMessageById(id);
    }

    /**
     * Retrieve the list of event message for a specific identifier
     *
     * @param id identifier of the event
     * @return the list of event messages corresponding
     */
    public List<EventMessage> getAllCommentsByEventId(long id) {
        return repo.getAllMessagesByEventId(id);
    }
}
