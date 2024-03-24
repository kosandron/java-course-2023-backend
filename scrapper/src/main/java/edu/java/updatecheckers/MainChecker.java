package edu.java.updatecheckers;

import edu.java.domain.models.Link;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("DefaultUpdateChecker")
@RequiredArgsConstructor
public class MainChecker {
    private final List<UpdateChecker> updateCheckers;

    public UpdateCheckResult check(Link link) {
        for (UpdateChecker checker : updateCheckers) {
            UpdateCheckResult result = checker.check(link);
            if (result.hasUpdate()) {
                return result;
            }
        }

        return UpdateCheckResult.getDefault();
    }
}
