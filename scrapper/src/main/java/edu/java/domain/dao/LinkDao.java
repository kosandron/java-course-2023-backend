package edu.java.domain.dao;

import edu.java.domain.model.Link;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkDao {
    private final JdbcTemplate jdbcTemplate;

    public Link save(Link link) {
        jdbcTemplate.update(
            """
                INSERT INTO links (url, last_check_time, last_modified_time)
                VALUES (?, ?, ?);
                """, link.getUrl(), link.getLastCheckTime(), link.getLastModifiedTime());
        return findByUrl(link.getUrl()).get();
    }

    public void updateLink(Link link) {
        jdbcTemplate.update(
            "UPDATE links SET last_modified = ?, last_checked = ? WHERE id = ?",
            link.getLastModifiedTime(), link.getLastCheckTime(), link.getId()
        );
    }

    public int deleteById(long linkId) {
        return jdbcTemplate.update("DELETE FROM links WHERE id = ?", linkId);
    }

    public Optional<Link> findById(long linkId) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM links WHERE id = ?",
                    (rs, rowNum) -> Link.parseResultSet(rs),
                    linkId
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Link> findByUrl(String url) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM links WHERE url = ?",
                    (rs, rowNum) -> Link.parseResultSet(rs),
                    url
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Collection<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM links", (rs, rowNum) -> Link.parseResultSet(rs));
    }

    public Collection<Link> findNLinksByOldestLastCheck(int count) {
        return jdbcTemplate.query(
            "SELECT * FROM links ORDER BY last_check_time LIMIT ?",
            (rs, rowNum) -> Link.parseResultSet(rs),
            count
        );
    }
}
