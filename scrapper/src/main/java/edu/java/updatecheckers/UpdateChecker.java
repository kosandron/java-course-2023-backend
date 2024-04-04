package edu.java.updatecheckers;

import edu.java.dto.database.LinkDto;

public interface UpdateChecker {
    UpdateCheckResult check(LinkDto link);

    boolean supports(String linkUrl);
}
