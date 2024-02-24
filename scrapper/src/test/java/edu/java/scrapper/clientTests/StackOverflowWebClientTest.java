package edu.java.scrapper.clientTests;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.StackOverFlowResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
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
public class StackOverflowWebClientTest {
    private static final String QUESTION_TITLE = "Room DataBase not implemented";
    private static final Long QUESTION_ID = 44162951L;
    private static final String URL = "/questions/" + QUESTION_ID;
    private static final OffsetDateTime LAST_MODIFIED_TIME =
        OffsetDateTime.of(2024, 2, 24, 17, 3, 3, 0, ZoneOffset.UTC);

    private final StackOverflowClient client = new StackOverflowWebClient("http://localhost:5454");

    public void makeStubSuccess() {
        stubFor(get(urlEqualTo(URL))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                            "title": "%s",
                            "last_activity_date": %d
                        }
                        """.formatted(QUESTION_TITLE, LAST_MODIFIED_TIME.toEpochSecond()))
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
        StackOverFlowResponse response = client.fetchQuestion(QUESTION_ID);

        // Assert
        assertThat(response.title()).isEqualTo(QUESTION_TITLE);
        assertThat(response.lastModified()).isEqualTo(LAST_MODIFIED_TIME);
    }

    @Test
    public void badRequestTest() {
        // Arrange
        makeStubUserError();

        // Act, Assert
        assertThrows(WebClientResponseException.class, () -> client.fetchQuestion(QUESTION_ID));
    }
}
