package edu.java.services;

import edu.java.domain.model.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, String url);

    Link remove(long tgChatId, String url);

    List<Link> listByOldestCheck(int count);

    List<Link> listAll();

    void updateLastModified(long linkId, OffsetDateTime offsetDateTime);

    void updateLastChecked(long linkId, OffsetDateTime offsetDateTime);
}
