package edu.java.services;

import edu.java.dto.database.LinkDto;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    LinkDto add(long chatId, String url);

    LinkDto remove(long chatId, String url);

    List<LinkDto> listByOldestCheck(int count);

    List<LinkDto> listAll();

    void updateLastModified(long linkId, OffsetDateTime offsetDateTime);

    void updateLastChecked(long linkId, OffsetDateTime offsetDateTime);
}
