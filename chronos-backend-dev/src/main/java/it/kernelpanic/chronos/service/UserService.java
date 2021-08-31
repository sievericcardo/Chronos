package it.kernelpanic.chronos.service;

import it.kernelpanic.chronos.dao.UserDao;
import it.kernelpanic.chronos.model.FakeUser;
import it.kernelpanic.chronos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User service class.
 * We don't distinguish between user that perform operation via Google or with
 * the server login/registration form, we have the different models that will
 * handle things accordingly, but with a single service, allowing transparency
 * for users.
 */
@Component
public class UserService {

    @Autowired
    private UserDao repo;

    /**
     * Service that create a new record in the user table. It propagates the changes by invoking the method
     * in the dao
     *
     * @param user object to be added
     * @return true if it succeeded
     */
    public boolean add(User user) {
        return repo.create(user);
    }

    /**
     * Delete a specific user from the table
     *
     * @param id user identifier
     * @return true if it succeeded
     */
    public boolean delete(Long id) {
        return repo.delete(id);
    }

    /**
     * Retrieve the security secret string for a user
     *
     * @param uname username value of the user
     * @return the secret string
     */
    public String getSecretByUsername(String uname) {
        return repo.getSecretByUsername(uname);
    }

    /**
     * Retrieve the avatar for a specific user
     *
     * @param uname username value of the user
     * @return the path for the avatar as a string
     */
    public String getAvatarByUsername(String uname) {
        return repo.getAvatarByUsername(uname);
    }

    /**
     * Retrieve a user by its id
     *
     * @param id user identifier
     * @return the corresponding user
     */
    public User getUserById(Long id) {
        return repo.getUserById(id);
    }

    /**
     * Update a user record
     *
     * @param user object to be updated
     * @return true if it succeeded
     */
    public boolean update(User user) {
        return repo.update(user);
    }

    /**
     * Retrieve a specific user from its username
     *
     * @param username unique name for the user to be used for lookup
     * @return the user identifier
     */
    public long getUserIdByUsername(String username) {
        return repo.getUserIdByUsername(username);
    }

    /**
     * Service that create a new fake user. It propagates the changes by invoking the method
     * in the dao
     *
     * @param fakeUser object to be added
     * @return true if it succeeded
     */
    public boolean addFakeUser(FakeUser fakeUser) {
        return repo.addFakeUser(fakeUser);
    }
}
