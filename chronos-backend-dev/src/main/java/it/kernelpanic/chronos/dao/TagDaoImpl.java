package it.kernelpanic.chronos.dao;

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
import java.util.Map;

/**
 * Class that implements the TagDao interface. We allow users to perform
 * CRUD operations on the Tag table.
 * We define the queries that will be used to perform the operations on the
 * database.
 */
@Component
public class TagDaoImpl implements TagDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create a record of Tag in the corresponding table. We map the objects into a hashmap.
     *
     * @param tag object of type tag to be inserted in the database
     * @return true if it succeeded
     */
    @Override
    public boolean create(Tag tag) {
        String INSERT_QUERY = "INSERT INTO ch_tag.tag (name) VALUES (:name)";
        Map<String, Object> map = new HashMap<>();
        map.put("name", tag.getName());

        return namedParamJdbcTemplate.update(INSERT_QUERY, map) > 0;
    }

    /**
     * Retrieve the specific tag from the database
     *
     * @param id identifier for the tag retrieval
     * @return the single tag
     */
    @Override
    public Tag getTagNameById(Long id) {
        String SELECT_BY_ID_QUERY = "SELECT id,name FROM ch_tag.tag WHERE id = :id";
        return namedParamJdbcTemplate.queryForObject(SELECT_BY_ID_QUERY, new MapSqlParameterSource(
                "id", id), new TagMapper());
    }

    /**
     * Delete a tag record from the corresponding table
     *
     * @param id tag identifier
     * @return true if it succeeded
     */
    @Override
    public boolean delete(Long id) {
        //attenzione che Tag è anche chiave esterna di Classification, quindi va configurata una
        //cascade della madonna
        return false;
    }

    @Override
    public void cleanup() {
    }

    private static final class TagMapper implements RowMapper<Tag> {
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag t = new Tag();
            t.setId(rs.getLong("id"));
            t.setName(rs.getString("name"));
            return t;
        }
    }
}
