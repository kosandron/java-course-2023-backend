package edu.java.scrapper.clientTests;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.client.dto.StackOverFlowResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import edu.java.configuration.RetryConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 5454)
@SpringBootTest(classes = {RetryConfig.class})
@ContextConfiguration(initializers = StackOverflowWebClientTest.Initializer.class)
public class StackOverflowWebClientTest {
    private static final String QUESTION_TITLE = "Room DataBase not implemented";
    private static final Long QUESTION_ID = 44162951L;
    private static final String URL = "/questions/" + QUESTION_ID;
    private static final OffsetDateTime LAST_MODIFIED_TIME =
        OffsetDateTime.of(2024, 2, 24, 17, 3, 3, 0, ZoneOffset.UTC);
    private final static int RETRY_COUNT = 5;

    private final StackOverflowClient client;

    @Autowired
    public StackOverflowWebClientTest(Retry retry) {
        client = new StackOverflowWebClient("http://localhost:5454", retry);
    }

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

    @Test
    public void successFetchAfterRequestsTest() {
        // Arrange
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(get(urlEqualTo(URL))
                .inScenario("RETRY")
                .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(aResponse().withStatus(501))
                .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(get(urlEqualTo(URL))
            .inScenario("RETRY")
            .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
            .willReturn(aResponse().withStatus(200).withBody(""))
        );

        // Act, Assert
        assertDoesNotThrow(() -> client.fetchQuestion(1L));
        verify(RETRY_COUNT, getRequestedFor(urlEqualTo(URL)));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=fixed").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=1000").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502").applyTo(applicationContext);
        }
    }
}
