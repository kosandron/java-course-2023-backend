package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.configuration.RetryConfig;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonSchema;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 1234)
@SpringBootTest(classes = {RetryConfig.class})
@ContextConfiguration(initializers = ScrapperLinksHttpClientTests.Initializer.class)
public class ScrapperLinksHttpClientTests {
    private static final int RETRY_COUNT = 5;
    private static final String URL1 = "https://github.com/kosandron/java-course-2023";
    private static final String URL2 = "https://github.com/kosandron/java-course-2023-backend";

    private final ScrapperLinksHttpClient client;

    @Autowired
    public ScrapperLinksHttpClientTests(Retry retry) {
        client = new ScrapperLinksHttpClient("http://localhost:1234", retry);
    }

    @Test
    public void successGetLinksRequestTest() {
        // Arrange
        stubFor(
            get(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .withHeader("Tg-Chat-Id", equalTo("1"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(jsonToStr("src/test/resources/scrapper/links.json").formatted(URL1, URL2)
                    )
                )
        );

        // Act
        var response = client.getLinks(1L);

        // Assert
        assertThat(response.links())
            .contains(new LinkResponse(1L, URI.create(URL1)))
            .contains(new LinkResponse(2L, URI.create(URL2)));
        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    public void successAddLinkRequestTest() {
        // Arrange
        stubFor(
            post(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .withHeader("Tg-Chat-Id", equalTo("1"))
                .withRequestBody(matchingJsonSchema(
                        """
                        {
                            "link": "%s"
                        }
                        """.formatted(URL1)
                    )
                )
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(
                        """
                        {
                            "id": 1,
                            "url": "%s"
                        }
                        """.formatted(URL1)
                    )
                )
        );

        // Act
        var response = client.addLink(1L, URL1);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.url()).isEqualTo(URI.create(URL1));
    }

    @Test
    public void successDeleteLinkRequestTest() {
        // Arrange
        stubFor(
            delete(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .withHeader("Tg-Chat-Id", equalTo("1"))
                .withRequestBody(matchingJsonSchema(
                        """
                        {
                            "link": "%s"
                        }
                        """.formatted(URL1)
                    )
                )
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(
                        """
                        {
                            "id": 1,
                            "url": "%s"
                        }
                        """.formatted(URL1)
                    ))
        );

        // Act
        var response = client.deleteLink(1L, URL1);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.url()).isEqualTo(URI.create(URL1));
    }


    @Test
    public void badGetRequestTest() {
        // Arrange
        stubFor(
            get(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );
        stubFor(
            post(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );
        stubFor(
            delete(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );

        // Act, Assert
        assertThrows(WebClientResponseException.class, () -> client.getLinks(1L));
        assertThrows(WebClientResponseException.class, () -> client.addLink(1L, URL1));
        assertThrows(WebClientResponseException.class, () -> client.deleteLink(1L, URL1));
    }

    @SneakyThrows
    public String jsonToStr(String filepath) {
        return Files.readString(Paths.get(filepath));
    }

    @Test
    public void successGetLinksAfterRetriesRequestTest() {
        // Arrange
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(get(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .inScenario("RETRY")
                .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(aResponse().withStatus(429))
                .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(get(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
            .inScenario("RETRY")
            .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
            .willReturn(aResponse().withStatus(200).withBody("")
        ));

        // Act, Assert
        assertDoesNotThrow(()-> client.getLinks(1L));
        verify(RETRY_COUNT, getRequestedFor(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH)));
    }

    @Test
    public void successAddLinkAfterRetriesRequestTest() {
        // Arrange
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(post(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .inScenario("RETRY")
                .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(aResponse().withStatus(429))
                .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(post(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
            .inScenario("RETRY")
            .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
            .willReturn(aResponse().withStatus(200).withBody("")
            ));

        // Act, Assert
        assertDoesNotThrow(()-> client.addLink(1L, URL1));
        verify(RETRY_COUNT, postRequestedFor(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH)));
    }

    @Test
    public void successDeleteLinkAfterRetriesRequestTest() {
        // Arrange
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(delete(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
                .inScenario("RETRY")
                .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(aResponse().withStatus(429))
                .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(delete(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH))
            .inScenario("RETRY")
            .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
            .willReturn(aResponse().withStatus(200).withBody("")
            ));

        // Act, Assert
        assertDoesNotThrow(()-> client.deleteLink(1L, URL1));
        verify(RETRY_COUNT, deleteRequestedFor(urlEqualTo(ScrapperLinksHttpClient.LINKS_PATH)));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=fixed").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=500").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502").applyTo(applicationContext);
        }
    }
}
