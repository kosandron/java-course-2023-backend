package edu.java.services;

import edu.java.domain.models.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    Link add(long chatId, String url);

    Link remove(long chatId, String url);

    List<Link> listByOldestCheck(int count);

    List<Link> listAll();

    void updateLastModified(long linkId, OffsetDateTime offsetDateTime);

    void updateLastChecked(long linkId, OffsetDateTime offsetDateTime);
}
