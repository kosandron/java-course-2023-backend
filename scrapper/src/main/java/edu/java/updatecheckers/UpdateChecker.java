package edu.java.updatecheckers;

import edu.java.domain.models.Link;

public interface UpdateChecker {
    UpdateCheckResult check(Link link);

    boolean supports(String linkUrl);
}
