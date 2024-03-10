package edu.java.bot.repositories;

import java.util.List;
import org.apache.commons.validator.routines.UrlValidator;

public interface LinkRepository {

    List<String> getTrackedLinks(long telegramId);

    boolean trackLink(long telegramId, String link);

    boolean untrackLink(long telegramId, String link);
}
