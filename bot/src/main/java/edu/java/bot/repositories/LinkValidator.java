package edu.java.bot.repositories;

import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.UrlValidator;

@UtilityClass
public class LinkValidator {
    public boolean isValidLink(String link) {
        var urlValidator = new UrlValidator();
        return urlValidator.isValid(link);
    }
}
