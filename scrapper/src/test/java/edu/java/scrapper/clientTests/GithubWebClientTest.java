package edu.java.scrapper.clientTests;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.GithubResponse;
import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 5454)
public class GithubWebClientTest {
    private static final String REPOSITORY_OWNER = "kosandron";
    private static final String REPOSITORY_NAME = "java-course-2023";
    private static final String URL = "/repos/" + REPOSITORY_OWNER + "/" + REPOSITORY_NAME;
    private static final OffsetDateTime LAST_MODIFIED_TIME =
        OffsetDateTime.of(2024, 2, 24, 17, 3, 3, 0, ZoneOffset.UTC);

    private final GitHubClient client = new GitHubWebClient("http://localhost:5454");

    public void makeStubSuccess() {
        stubFor(get(urlEqualTo(URL))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                            "title": "%s",
                            "updated_at": %d
                        }
                    """.formatted(REPOSITORY_NAME, LAST_MODIFIED_TIME.toEpochSecond()))
            ));
    }

    public void makeStubUserError() {
        stubFor(get(urlEqualTo(URL))
            .willReturn(aResponse()
                .withStatus(404)
            ));
    }

    @Test
    public void successRequestTest() {
        // Arrange
        makeStubSuccess();

        // Act
        GithubResponse response = client.fetchRepository(REPOSITORY_OWNER, REPOSITORY_NAME);

        // Assert
        assertThat(response.title()).isEqualTo(REPOSITORY_NAME);
        assertThat(response.lastModified()).isEqualTo(LAST_MODIFIED_TIME);
    }

    @Test
    public void badRequestTest() {
        // Arrange
        makeStubUserError();

        // Act, Assert
        assertThrows(WebClientResponseException.class, () -> client.fetchRepository(REPOSITORY_OWNER, REPOSITORY_NAME));
    }
}
