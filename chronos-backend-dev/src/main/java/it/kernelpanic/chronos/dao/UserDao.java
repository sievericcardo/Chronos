package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.FakeUser;
import it.kernelpanic.chronos.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.sql.DataSource;

/**
 * Interface for the user table. We need to handle both login and
 * registration part for a user inside the system.
 * The user itself will not invoke directly any methods, but we
 * must ensure the possible CRUD operations.
 */
public interface UserDao {
    // Data source for the connection to the db
    void setDataSource(DataSource ds);

    String getAvatarByUsername(String username);

    String getSecretByUsername(String username);

    /**
     * Create a user record in the corresponding table
     *
     * @param user object of type user to be inserted in the database
     * @return true if it succeeded
     */
    boolean create(User user);

    /**
     * Retrieve a specific user
     *
     * @param id identifier for the user retrieval
     * @return the user object
     */
    User getUserById(Long id);

    /**
     * Delete a specific user from the database
     *
     * @param id user identifier for the removal
     * @return true if it succeeded
     */
    boolean delete(Long id);

    /**
     * Update a specific user from the database
     *
     * @param user object that need to be updated
     * @return true if it succeeded
     */
    boolean update(User user);

    /**
     * Retrieve the user id by the username
     *
     * @param username unique username of the user
     * @return the id
     */
    long getUserIdByUsername(String username);

    /**
     * Retrieve the user parameters by its username
     * @param username unique username of the user
     * @return the user details
     */
    UserDetails getUserByUsername(String username);

    /**
     * Add the user for the google authentication part
     *
     * @param fakeUser object of the user for the authentication
     * @return true if it succeeded
     */
    boolean addFakeUser(FakeUser fakeUser);

    void cleanup();
}
