package edu.java.client.github;

import edu.java.client.dto.GithubResponse;
import org.jetbrains.annotations.NotNull;

public interface GitHubClient {
    GithubResponse fetchRepository(@NotNull String owner, @NotNull String repository);
}
