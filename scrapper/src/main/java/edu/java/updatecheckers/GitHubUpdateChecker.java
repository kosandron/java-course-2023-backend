package edu.java.updatecheckers;

import edu.java.client.dto.GithubResponse;
import edu.java.client.github.GitHubClient;
import edu.java.dto.database.LinkDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubUpdateChecker implements UpdateChecker {
    private static final int MIN_WORDS_COUNT = 4;
    private static final int OWNER_POSITION = 2;
    private static final int REPOSITORY_POSITION = 2;
    private static final String GITHUB_DOMAIN = "github.com";
    private final GitHubClient gitHubClient;

    @Override
    public UpdateCheckResult check(LinkDto link) {
        if (supports(link.getUrl())) {
            List<String> extensions = PathParser.getPathComponents(link.getUrl());
            if (extensions.size() < MIN_WORDS_COUNT) {
                return UpdateCheckResult.getDefault();
            }

            GithubResponse response = gitHubClient
                .fetchRepository(extensions.get(OWNER_POSITION), extensions.get(REPOSITORY_POSITION));
            if (link.getLastModified().isBefore(response.lastModified())) {
                link.setLastModified(response.lastModified());
                return new UpdateCheckResult(true, response.title());
            }
        }

        return UpdateCheckResult.getDefault();
    }

    @Override
    public boolean supports(String url) {
        return url.contains(GITHUB_DOMAIN);
    }
}
