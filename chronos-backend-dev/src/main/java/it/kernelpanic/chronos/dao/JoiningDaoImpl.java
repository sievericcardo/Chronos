package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Event;
import it.kernelpanic.chronos.model.EventResponse;
import it.kernelpanic.chronos.model.Joining;
import it.kernelpanic.chronos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the JoiningDao interface. We define the queries for the
 * CRUD operations on the Joining table on the database.
 * We establish a connection to the database with the JDBC connector.
 */
@Component
public class JoiningDaoImpl implements JoiningDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create the record of joining in the corresponding table. We insert both user id and event id since we use the
     * couple of them as primary key
     *
     * @param event_id identifier of the event to which a user participate
     * @param user_id identifier of the user that participates
     * @return true if it succeeded
     */
    @Override
    public boolean create(long event_id, long user_id) {
        String INSERT_QUERY = "INSERT INTO ch_joining.joining(event_id, user_id) " +
                "VALUES (:event_id,:user_id)";
        Map<String, Object> map = new HashMap<>();
        map.put("event_id", event_id);
        map.put("user_id", user_id);

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;
    }

    /**
     * Get the list of joinings of events for a given user. We need to join the joining table with the event table on
     * the event id
     *
     * @param id identifier of user
     * @return the list of joinings
     */
    @Override
    public List<EventResponse> getAllJoiningsByUserId(long id) {

        String SELECT_ALL_BY_USER_ID_QUERY = "SELECT j.event_id as eid, e.name as name, e.event_when as wh, u.username as username, u.avatar as user_avatar " +
                ",e.landing_image as landing_image ,e.created_at, e.created_by ,e.location, e.longitude as longitude, e.latitude as latitude , e.description as description FROM ch_joining.joining j " +
                "JOIN ch_event.event e ON e.id = j.event_id " +
                "JOIN ch_user.user u ON u.id = e.created_by " +
                "WHERE j.user_id = :user_id ORDER BY e.event_when ASC";
        return namedParamJdbcTemplate.query(SELECT_ALL_BY_USER_ID_QUERY, new MapSqlParameterSource(
                "user_id", id), new EventResponseMapper());

    }

    /**
     * Get the list of joinings of events for a given event. We need to join the joining table with the user table on
     * the user id
     *
     * @param id identifier of event
     * @return the list of joinings
     */
    @Override
    public List<User> getAllJoiningsByEventId(long id) {
        String SELECT_ALL_BY_USER_ID_QUERY = "SELECT j.user_id as id, u.username, u.avatar  FROM ch_joining.joining j " +
                "JOIN ch_user.user u ON u.id = j.user_id WHERE j.event_id = :event_id";
        return namedParamJdbcTemplate.query(SELECT_ALL_BY_USER_ID_QUERY, new MapSqlParameterSource(
                "event_id", id), new UserMapper());
    }

    /**
     * Remove a specific record from the table
     *
     * @param event_id event identifier for the joining
     * @param user_id user identifier for the joining
     * @return true if it succeeded
     */
    @Override
    public boolean delete(long user_id, long event_id) {
        String DELETE_QUERY = "DELETE FROM ch_joining.joining WHERE user_id = :user_id AND event_id = :event_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("user_id", user_id).addValue("event_id", event_id);
        int status = namedParamJdbcTemplate.update(DELETE_QUERY, namedParameters);

        return status != 0;
    }

    // TODO: generate it for future use
    @Override
    public boolean update(Joining join) {
        return false;
    }


    @Override
    public void cleanup() {
    }


    private static final class EventMapper implements RowMapper<Event> {
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event e = new Event();
            e.setId(rs.getLong("eid"));
            e.setName(rs.getString("name"));
            e.setEvent_when(rs.getLong("wh"));
            return e;
        }
    }

    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setUsername(rs.getString("username"));
            u.setAvatar(rs.getString("avatar"));
            u.setId(rs.getLong("id"));
            return u;
        }
    }

    private static final class EventResponseMapper implements RowMapper<EventResponse> {
        public EventResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventResponse e = new EventResponse();
            e.setEvent_id(rs.getLong("eid"));
            e.setName(rs.getString("name"));
            e.setDescription(rs.getString("description"));
            e.setCreated_at(rs.getLong("created_at"));
            e.setLocation(rs.getString("location"));
            e.setLatitude(rs.getDouble("latitude"));
            e.setLongitude(rs.getDouble("longitude"));
            e.setCreated_by(rs.getLong("created_by"));
            e.setLanding_image(rs.getString("landing_image"));
            e.setEvent_when(rs.getLong("wh"));
            e.setUsername(rs.getString("username"));
            e.setUser_avatar(rs.getString("user_avatar"));
            return e;
        }
    }
}
