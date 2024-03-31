package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.bot.configuration.RetryConfig;
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
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 1234)
@SpringBootTest(classes = {RetryConfig.class})
@ContextConfiguration(initializers = ScrapperTgChatHttpClientTests.Initializer.class)
public class ScrapperTgChatHttpClientTests {
    private static final int RETRY_COUNT = 5;
    private final ScrapperTgChatHttpClient client;

    @Autowired
    public ScrapperTgChatHttpClientTests(Retry retry) {
        client = new ScrapperTgChatHttpClient("http://localhost:1234", retry);
    }

    @Test
    public void successRequestTest() {
        // Arrange
        stubFor(
            post(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
                .withPathParam("id", equalTo("1"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(""))
        );
        stubFor(
            delete(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
                .withPathParam("id", equalTo("1"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(""))
        );

        // Act, Assert
        assertDoesNotThrow(() -> client.addTgChat(1L));
        assertDoesNotThrow(() -> client.deleteTgChat(1L));
    }

    @Test
    public void badRequestTest() {
        // Arrange
        stubFor(
            post(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
                .withPathParam("id", equalTo("0"))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );
        stubFor(
            delete(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
                .withPathParam("id", equalTo("0"))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );

        // Act, Assert
        assertThrows(WebClientResponseException.class, () -> client.addTgChat(0L));
        assertThrows(WebClientResponseException.class, () -> client.deleteTgChat(0L));
    }

    @Test
    public void successAddAfterRequestsTest() {
        // Arrange
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(post(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
                .withPathParam("id", equalTo("1"))
                .inScenario("RETRY")
                .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(aResponse().withStatus(502))
                .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(post(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
            .withPathParam("id", equalTo("1"))
            .inScenario("RETRY")
            .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
            .willReturn(aResponse().withStatus(200).withBody(""))
        );

        // Act, Assert
        assertDoesNotThrow(() -> client.addTgChat(1L));
        verify(RETRY_COUNT, postRequestedFor(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK)).withPathParam("id", equalTo("1")));
    }

    @Test
    public void successDeleteAfterRequestsTest() {
        // Arrange
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(delete(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
                .withPathParam("id", equalTo("1"))
                .inScenario("RETRY")
                .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(aResponse().withStatus(502))
                .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(delete(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK))
            .withPathParam("id", equalTo("1"))
            .inScenario("RETRY")
            .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
            .willReturn(aResponse().withStatus(200).withBody(""))
        );

        // Act, Assert
        assertDoesNotThrow(() -> client.deleteTgChat(1L));
        verify(RETRY_COUNT, deleteRequestedFor(urlPathTemplate(ScrapperTgChatHttpClient.TG_CHAT_BY_ID_LINK)).withPathParam("id", equalTo("1")));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=exponential").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=500").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502").applyTo(applicationContext);
        }
    }
}
