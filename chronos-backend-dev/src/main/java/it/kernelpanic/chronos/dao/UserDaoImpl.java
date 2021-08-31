package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.FakeUser;
import it.kernelpanic.chronos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that implements the UserDao interface. We handle the queries that allows
 * a user to register into the system (insertion of a new user), login (lookup
 * of a single user).
 * Every operation need to be propagated to the database via JDBC
 */
@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create a user record in the corresponding table. We map each parameter from the object into the database
     *
     * @param user object of type user to be inserted in the database
     * @return true if it succeeded
     */
    @Override
    public boolean create(User user) {
        String INSERT_QUERY = "INSERT INTO ch_user.user (username, email, password, security_parameters, auth_provider, avatar) " +
                "VALUES (:username, :email, :password, :security_parameters, :auth_provider, :avatar)";
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("password", new Argon2PasswordEncoder().encode(user.getPassword()));
        map.put("security_parameters", user.getSecurity_parameters());
        map.put("auth_provider", user.getAuth_provider());
        map.put("avatar", user.getAvatar());

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;

    }

    /**
     * Retrieve a specific user
     *
     * @param id identifier for the user retrieval
     * @return the user object
     */
    @Override
    public User getUserById(Long id) {
        String SELECT_BY_ID_QUERY = "SELECT username,email,password,avatar,security_parameters FROM ch_user.user WHERE id = :id";
        return namedParamJdbcTemplate.queryForObject(SELECT_BY_ID_QUERY, new MapSqlParameterSource(
                "id", id), new UserMapper());
    }

    /**
     * Delete a specific user from the database
     *
     * @param id user identifier for the removal
     * @return true if it succeeded
     */
    @Override
    public boolean delete(Long id) {
        String DELETE_QUERY = "DELETE FROM ch_user.user WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

        return namedParamJdbcTemplate.update(DELETE_QUERY, namedParameters) != 0;
    }

    /**
     * Update a specific user from the database as part of the CRUD operations
     *
     * @param user object that need to be updated
     * @return true if it succeeded
     */
    @Override
    public boolean update(User user) {
        String UPDATE_QUERY = "UPDATE ch_user.user SET email = :email WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", user.getEmail()).addValue("id", user.getId());

        return namedParamJdbcTemplate.update(UPDATE_QUERY, namedParameters) > 0;
    }

    /**
     * Retrieve the user id by the username
     *
     * @param username unique username of the user
     * @return the id
     */
    @Override
    public long getUserIdByUsername(String username) {
        String SELECT_BY_USERNAME_QUERY = "SELECT id FROM ch_user.user WHERE username = :username";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("username", username);
        try {
            return namedParamJdbcTemplate.queryForObject(SELECT_BY_USERNAME_QUERY, namedParameters, Long.class);
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Retrieve the user parameters by its username
     * @param username unique username of the user
     * @return the user details
     */
    @Override
    public UserDetails getUserByUsername(String username) {
        String SELECT_BY_USERNAME_QUERY = "SELECT id,username,email,password,security_parameters,avatar FROM ch_user.user WHERE username = :username";
        User u = namedParamJdbcTemplate.queryForObject(SELECT_BY_USERNAME_QUERY, new MapSqlParameterSource(
                "username", username), new UserMapper());

        assert u != null;
        return buildUserFromUserEntity(u);
    }

    @Override
    public void cleanup() {

    }

    /**
     * Retrieve the security parameter of a specific user given the username
     *
     * @param username unique username of the user
     * @return the security parameter as string
     */
    public String getSecretByUsername(String username) {
        String SELECT_SECRET_BY_USERNAME_QUERY = "SELECT security_parameters FROM ch_user.user WHERE username = :username";
        String u = namedParamJdbcTemplate.queryForObject(SELECT_SECRET_BY_USERNAME_QUERY, new MapSqlParameterSource(
                "username", username), String.class);

        assert u != null;
        return u;
    }

    /**
     * Retrieve the avatar path of a specific user given the username
     *
     * @param username unique username of the user
     * @return the avatar path as a string
     */
    public String getAvatarByUsername(String username) {
        String SELECT_SECRET_BY_USERNAME_QUERY = "SELECT avatar FROM ch_user.user WHERE username = :username";
        String u = namedParamJdbcTemplate.queryForObject(SELECT_SECRET_BY_USERNAME_QUERY, new MapSqlParameterSource(
                "username", username), String.class);

        assert u != null;
        return u;
    }

    /**
     * Add the user for the google authentication part
     *
     * @param fakeUser object of the user for the authentication
     * @return true if it succeeded
     */
    public boolean addFakeUser(FakeUser fakeUser) {
        String INSERT_QUERY = "INSERT INTO ch_user.user (username, email, password, security_parameters, auth_provider, avatar) " +
                "VALUES (:username, :email, :password, :security_parameters, :auth_provider, :avatar)";

        Map<String, Object> map = new HashMap<>();
        map.put("username", fakeUser.getUsername());
        map.put("email", fakeUser.getEmail());
        map.put("password", new Argon2PasswordEncoder().encode(fakeUser.getPassword()));
        map.put("security_parameters", fakeUser.getSecret_key());
        map.put("auth_provider", "google");
        map.put("avatar", fakeUser.getPicture());

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;

    }

    private UserDetails buildUserFromUserEntity(User userEntity) {
        // convert model user to spring security user
        String username = userEntity.getUsername();
        String password = userEntity.getPassword();
        List<GrantedAuthority> authList = new ArrayList<>(1);

        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(username,
                password,
                true,
                true,
                true,
                true,
                authList);
    }

    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setUsername(rs.getString("username"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setAvatar(rs.getString("avatar"));
            u.setSecurity_parameters(rs.getString("security_parameters"));
            return u;
        }
    }
}
