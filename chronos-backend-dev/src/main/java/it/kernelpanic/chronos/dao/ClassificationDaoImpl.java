package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Classification;
import it.kernelpanic.chronos.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing the ClassificationDao interface.
 * We define the single queries that affects the Classification table on the
 * database.
 * Invocation are done using the JDBC parameter that will handle the connection
 * to the database via the configuration strings defined in the app config.
 */
@Component
public class ClassificationDaoImpl implements ClassificationDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create a new element in the table Classification. We implement the query for the insertion of the classification
     *
     * @param classification classification object
     * @return true if is succeeded
     */
    @Override
    public boolean create(Classification classification) {
        String INSERT_QUERY = "INSERT INTO ch_classification.classification(tag_id, event_id) " +
                "VALUES (:tag_id,:event_id)";
        Map<String, Object> map = new HashMap<>();
        map.put("tag_id", classification.getTag_id());
        map.put("user_id", classification.getEvent_id());

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;
    }

    /**
     * Return the list of classification. We do a join between the classification and the event table to retrieve
     * all the classification given a specific id
     *
     * @param id event identifier
     * @return the list of classification
     */
    @Override
    public List<Tag> getAllTagsByEventId(Long id) {
        String SELECT_ALL_BY_EVENT_ID_QUERY = "SELECT tag_id, t.name as tag_name FROM ch_classification.classification" +
                " JOIN ch_tag.tag t ON tag_id = t.id where event_id = :event_id";
        return namedParamJdbcTemplate.query(SELECT_ALL_BY_EVENT_ID_QUERY, new MapSqlParameterSource(
                "event_id", id), new TagMapper());
    }

    // We will not include it in the documentation
    @Override
    public boolean delete(Long tag_id, Long event_id) {
        //Anche qui, è complicato per il discorso chiavi esterne
        // TODO: aggiungere la query
        return false;
    }

    @Override
    public void cleanup() {

    }

    private static final class TagMapper implements RowMapper<Tag> {
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag t = new Tag();
            t.setId(rs.getLong("tag_id"));
            t.setName(rs.getString("tag_name"));

            return t;
        }
    }
}
