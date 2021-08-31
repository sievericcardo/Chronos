package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.JoiningDao;
import it.kernelpanic.chronos.model.EventResponse;
import it.kernelpanic.chronos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Joining service class. We can create, and get events by passing single ids.
 * We can inspect joinings by loooking for specific events or specific users.
 */
@Component
public class JoiningService {

    @Autowired
    JoiningDao repo;

    /**
     * Service that create a new record in the joining table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param event_id identifier for the event a user participate to
     * @param user_id identifier for the user that participates to an event
     * @return true if it succeeded
     */
    public boolean create(long event_id, long user_id) {
        return repo.create(event_id, user_id);
    }

    /**
     * Retrieve the list of users that participate to an event
     *
     * @param id identifier of the event
     * @return the list of users
     */
    public List<User> getAllJoiningsByEventId(long id) {
        return repo.getAllJoiningsByEventId(id);
    }

    /**
     * Retrieve the list of events to which a user participate to
     *
     * @param id identifier of the user
     * @return the list of events corresponding
     */
    public List<EventResponse> getAllJoiningsByUserId(long id) {
        return repo.getAllJoiningsByUserId(id);
    }


    public boolean deleteJoining(long user_id, long event_id) {
        return repo.delete(user_id, event_id);
    }
}
