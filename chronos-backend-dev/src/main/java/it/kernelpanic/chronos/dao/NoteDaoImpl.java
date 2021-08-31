package it.kernelpanic.chronos.dao;

import it.kernelpanic.chronos.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Class that implements the methods for the NoteDao interface. Users can perform
 * the standard CRUD operations on their own data while we handle the security
 * part.
 */
@Component
public class NoteDaoImpl implements NoteDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Override
    public void setDataSource(DataSource ds) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    /**
     * Create a record in the event table. We map the color with a switch statement to insert the different color
     * in the note. We allow user to create note and assign it as color "cyan, green, magenta, and yellow"
     *
     * @param note object note to be used for the insertion in the table
     * @return true if it succeeded
     */
    @Override
    public long create(Note note) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        String INSERT_QUERY = "INSERT INTO ch_note.note(body,created_by,color) " +
                "VALUES (:body,:created_by,:color) RETURNING id";
        String color;
        switch (note.getColor()) {
            case 1:
                color = "cyan";
                break;
            case 2:
                color = "green";
                break;
            case 3:
                color = "magenta";
                break;
            default:
                color = "yellow";
                break;

        }
        SqlParameterSource namedParameters = new MapSqlParameterSource("body", note.getBody())
                .addValue("created_by", note.getCreated_by())
                .addValue("color", color);

        if (namedParamJdbcTemplate.update(INSERT_QUERY, namedParameters, holder) > 0) {
            return Objects.requireNonNull(holder.getKey()).longValue();
        }
        return -1;
    }

    /**
     * Retrieve the single note by a given event
     *
     * @param id identifier of the event to get the note
     * @return the corresponding note
     */
    @Override
    public Note getNoteById(Long id) {
        String SELECT_BY_ID_QUERY = "SELECT body,created_by,created_at,color FROM ch_note.note WHERE id = :id";
        return namedParamJdbcTemplate.queryForObject(SELECT_BY_ID_QUERY, new MapSqlParameterSource(
                "id", id), new NoteMapper());
    }

    /**
     * Retrieve the list of note for a specific user
     *
     * @param id user identifier
     * @return the list of notes for the given user id
     */
    @Override
    public List<Note> getAllNotesByUserId(Long id) {
        String SELECT_ALL_BY_USER_ID_QUERY = "SELECT id,body,created_by,created_at,color FROM ch_note.note where created_by = :user_id ORDER BY created_by DESC";
        return namedParamJdbcTemplate.query(SELECT_ALL_BY_USER_ID_QUERY, new MapSqlParameterSource(
                "user_id", id), new NoteMapper());
    }

    /**
     * Delete a note for a specific user. We check whether or not the user is correct one
     *
     * @param uid user identifier for the user check
     * @param id identifier of the note
     * @return true if it succeeded
     */
    @Override
    public boolean delete(Long uid, Long id) {
        String DELETE_QUERY = "DELETE FROM ch_note.note WHERE id = :id AND ch_note.note.created_by = :uid";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id).addValue("uid", uid);
        int status = namedParamJdbcTemplate.update(DELETE_QUERY, namedParameters);

        return status != 0;
    }

    /**
     * Update a note for a specific user; we need this as part of the CRUD operations on the note
     *
     * @param note object to be updated
     * @return true if it succeeded
     */
    @Override
    public boolean update(Note note) {
        String UPDATE_QUERY = "UPDATE ch_note.note SET body = :body, color = :color WHERE id = :id " +
                "AND ch_note.note.created_by = :cb";
        String def_color;
        switch (note.getColor()) {
            case 1:
                def_color = "cyan";
                break;
            case 2:
                def_color = "green";
                break;
            case 3:
                def_color = "magenta";
                break;
            default:
                def_color = "yellow";
                break;

        }
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("body", note.getBody()).addValue("id", note.getId())
                .addValue("cb", note.getCreated_by()).addValue("color", def_color);
        return namedParamJdbcTemplate.update(UPDATE_QUERY, namedParameters) > 0;
    }

    @Override
    public void cleanup() {

    }

    private static final class NoteMapper implements RowMapper<Note> {
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note n = new Note();
            n.setId(rs.getLong("id"));
            n.setBody(rs.getString("body"));
            n.setCreated_by(rs.getLong("created_by"));
            n.setCreated_at(rs.getLong("created_at"));
            switch (rs.getString("color")) {
                case "cyan":
                    n.setColor(1);
                    break;
                case "green":
                    n.setColor(2);
                    break;
                case "magenta":
                    n.setColor(3);
                    break;
                default:
                    n.setColor(0);
            }

            return n;
        }
    }
}
