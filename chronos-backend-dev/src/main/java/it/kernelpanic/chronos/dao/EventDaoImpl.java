package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Event;
import it.kernelpanic.chronos.model.EventResponse;
import it.kernelpanic.chronos.model.Tag;
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
 * Class implementing the EventDao interface. We define the single
 * and queries to perform CRUD operations on the Event table.
 * Communication with the database is enforced with the JDBC connector.
 */
@Component
public class EventDaoImpl implements EventDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create a new Event record inside the event table. We map every parameter for the record from the event object
     * passed as argument to the method
     *
     * @param event object that need to be used for the event record
     * @return true if it succeeded
     */
    @Override
    public boolean create(Event event) {
        String INSERT_QUERY = "INSERT INTO ch_event.event(name,description,latitude,longitude,created_by,location,landing_image,event_when) " +
                "VALUES (:name,:description,:latitude,:longitude,:created_by,:location,:landing_image,:event_when)";
        Map<String, Object> map = new HashMap<>();
        map.put("name", event.getName());
        map.put("description", event.getDescription());
        map.put("latitude", event.getLatitude());
        map.put("longitude", event.getLongitude());
        map.put("created_by", event.getCreated_by());
        map.put("location", event.getLocation());
        map.put("event_when", event.getEvent_when());
        map.put("landing_image", event.getLanding_image());

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;
    }

    /**
     * Get the event corresponding to its id
     *
     * @param id event identifier used to retrieve the event
     * @return the event corresponding
     */
    @Override
    public EventResponse getEventById(Long id) {
        String SELECT_BY_ID_QUERY = "SELECT e.id,name,description,created_at,latitude,location" +
                ",longitude,landing_image,created_by" +
                ",event_when, u.avatar as user_avatar,u.username as username FROM ch_event.event e " +
                " JOIN ch_user.user u ON e.created_by = u.id WHERE e.id=:id";

        return namedParamJdbcTemplate.queryForObject(SELECT_BY_ID_QUERY, new MapSqlParameterSource(
                "id", id), new EventResponseMapper());
    }

    /**
     * Get all the records of the table event joining to the user
     *
     * @return the list of events
     */
    @Override
    public List<EventResponse> getAllEvents() {
        String SELECT_ALL_QUERY = "SELECT e.id,name,description,created_at,latitude," +
                "location,longitude,landing_image,event_when,created_by," +
                "u.avatar as user_avatar,u.username as username FROM ch_event.event e" +
                " JOIN ch_user.user u ON e.created_by = u.id ORDER BY e.event_when ASC";

        return namedParamJdbcTemplate.query(SELECT_ALL_QUERY, new EventResponseMapper());
    }

    /**
     * Return the list of the events that are near to the position specified by the coordinate given in term of
     * latitude and longitude using an offset to compute it
     *
     * @param latitude used for the latitude coordinate
     * @param longitude used for the longitude coordinate
     * @return the list of events retrieved by the select
     */
    @Override
    public List<EventResponse> getNearEvents(double latitude, double longitude) {
        //Questa query dovrebbe ritornare gli eventi compresi in un range di 55km SN e 85km EO
        String SELECT_BY_NEIGHBORHOOD_QUERY = "SELECT name,description,created_at,latitude,location,longitude,landing_image,created_by,event_when FROM ch_event.event " +
                "WHERE latitude <=:latitude + 1 and longitude <= :longitude + 0.5";

        return namedParamJdbcTemplate.query(SELECT_BY_NEIGHBORHOOD_QUERY, new MapSqlParameterSource(
                "latitude", latitude).addValue("longitude", longitude), new EventResponseMapper());
    }

    /**
     * Add a list of tags for a specific event into the corresponding table
     *
     * @param list tags that need to be added for the event
     * @param event_id identifier of the event
     * @return true if it succeeded
     */
    @Override
    public boolean addTagListToEvent(List<Tag> list, Long event_id) {
        String INSERT_QUERY = "INSERT INTO ch_classification.classification(tag_id,event_id) " +
                "VALUES (:tag_id, :event_id)";
        boolean res = true;
        for (Tag t : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("tag_id", t.getId());
            map.put("event_id", event_id);

            res = res && namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;
        }

        return res;
    }

    /**
     * Delete a record of event in the database
     *
     * @param id event identifier
     * @return true if it succeeded
     */
    @Override
    public boolean delete(Long id) {
        String DELETE_QUERY = "DELETE FROM ch_event.event WHERE id = :id";
        return namedParamJdbcTemplate.update(DELETE_QUERY, new MapSqlParameterSource(
                "id", id)) != 0;
    }

    /**
     * Update the event. We update the record corresponding to the specific identifier
     *
     * @param event object passed as element to get the id
     * @return true if succeeded
     */
    @Override
    public boolean update(Event event) {
        String UPDATE_QUERY = "UPDATE ch_event.event SET name = :name, description = :description, event_when = :date WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", event.getName())
                .addValue("description", event.getDescription())
                .addValue("date", event.getEvent_when());

        return namedParamJdbcTemplate.update(UPDATE_QUERY, namedParameters) > 0;
    }

    @Override
    public void cleanup() {

    }

    //Classe per fare il mapping 1:1 con il resultset della query SQL
    private static final class EventResponseMapper implements RowMapper<EventResponse> {
        public EventResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventResponse e = new EventResponse();
            e.setEvent_id(rs.getLong("id"));
            e.setName(rs.getString("name"));
            e.setDescription(rs.getString("description"));
            e.setCreated_at(rs.getLong("created_at"));
            e.setLocation(rs.getString("location"));
            e.setLatitude(rs.getDouble("latitude"));
            e.setLongitude(rs.getDouble("longitude"));
            e.setCreated_by(rs.getLong("created_by"));
            e.setLanding_image(rs.getString("landing_image"));
            e.setEvent_when(rs.getLong("event_when"));
            e.setUsername(rs.getString("username"));
            e.setUser_avatar(rs.getString("user_avatar"));
            return e;
        }
    }
}
