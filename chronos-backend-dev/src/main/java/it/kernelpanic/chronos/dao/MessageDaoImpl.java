package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.EventMessage;
import it.kernelpanic.chronos.model.Message;
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
 * Class that implements the methods of the MessageDao interface. It contains
 * the SQL queries for the creation, select, update and delete of messages.
 * We use the JDBC driver to connect to our Database Postgre.
 */
@Component
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create a record in the corresponding table. We map every parameter from the argument and the insert them into
     * the record table
     *
     * @param msg object of type message that will be inserted in the table
     * @return true if it succeeded
     */
    @Override
    public boolean create(Message msg) {
        String INSERT_QUERY = "INSERT INTO ch_message.message(body,created_by,event_id,created_at) " +
                "VALUES (:body,:created_by,:event_id,:created_at)";
        Map<String, Object> map = new HashMap<>();
        map.put("body", msg.getBody());
        map.put("created_by", msg.getCreated_by());
        map.put("event_id", msg.getEvent_id());
        map.put("created_at", msg.getCreated_at());

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;
    }

    /**
     * Retrieve the message record from a specific id
     *
     * @param id identifier for the message that need to be retrieved
     * @return the message
     */
    @Override
    public Message getMessageById(Long id) {
        String SELECT_BY_ID_QUERY = "SELECT body,created_by,event_id,created_at FROM ch_message.message WHERE id = :id";
        return namedParamJdbcTemplate.queryForObject(SELECT_BY_ID_QUERY, new MapSqlParameterSource(
                "id", id), new MessageMapper());
    }

    /**
     * Retrieve all the records from the message table from a specific event id. We join our message table with the
     * user table.
     *
     * @param id event identifier
     * @return the list of event message
     */
    @Override
    public List<EventMessage> getAllMessagesByEventId(Long id) {
        String SELECT_ALL_BY_EVENT_ID_QUERY = "SELECT body,created_by,event_id,created_at,u.avatar as avatar, u.username as username " +
                "FROM ch_message.message m JOIN ch_user.user u " +
                "ON u.id = m.created_by where event_id = :event_id";
        return namedParamJdbcTemplate.query(SELECT_ALL_BY_EVENT_ID_QUERY, new MapSqlParameterSource(
                "event_id", id), new EventMessageMapper());
    }

    /**
     * Delete a message record from the corresponding table
     *
     * @param id message identifier
     * @return true if it succeeded
     */
    @Override
    public boolean delete(Long id) {
        String DELETE_QUERY = "DELETE FROM ch_message.message WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("user_id", id);
        int status = namedParamJdbcTemplate.update(DELETE_QUERY, namedParameters);

        return status != 0;
    }

    /**
     * Update a message record from the corresponding table
     *
     * @param msg object that will be updated
     * @return true if it succeeded
     */
    @Override
    public boolean update(Message msg) {
        String UPDATE_QUERY = "UPDATE ch_message.message SET body = :body WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("body", msg.getBody()).addValue("id", msg.getId());
        return namedParamJdbcTemplate.update(UPDATE_QUERY, namedParameters) > 0;
    }

    @Override
    public void cleanup() {

    }

    private static final class MessageMapper implements RowMapper<Message> {
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            Message m = new Message();
            m.setBody(rs.getString("body"));
            m.setCreated_at(rs.getLong("created_at"));
            m.setEvent_id(rs.getLong("event_id"));
            m.setCreated_by(rs.getLong("created_by"));

            return m;
        }
    }

    private static final class EventMessageMapper implements RowMapper<EventMessage> {
        public EventMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventMessage em = new EventMessage();
            em.setUsername(rs.getString("username"));
            em.setUser_avatar(rs.getString("avatar"));
            em.setEvent_id(rs.getLong("event_id"));
            em.setCreated_by(rs.getLong("created_by"));
            em.setBody(rs.getString("body"));
            em.setCreated_at(rs.getLong("created_at"));

            return em;
        }
    }
}
