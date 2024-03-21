package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.BotHttpClient;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 1234)
public class BotHttpClientTest {
    private static final String URL = "https://github.com/kosandron/java-course-2023";
    private final BotHttpClient client = new BotHttpClient("http://localhost:1234");

    public void makeStubSuccess() {
        stubFor(post(urlEqualTo(BotHttpClient.UPDATE_PATH))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("")));
    }

    public void makeStubServerError() {
        stubFor(get(urlEqualTo(BotHttpClient.UPDATE_PATH))
            .willReturn(aResponse()
                .withStatus(404)
            ));
    }

    @Test
    public void successRequestTest() {
        // Arrange
        makeStubSuccess();

        // Act, Assert
        assertDoesNotThrow(() -> client.update(
            1,
            URI.create(URL),
            "",
            new Long[] {1L}
        ));
    }

    @Test
    public void badRequestTest() {
        // Arrange
        makeStubServerError();

        // Act, Assert
        assertThrows(WebClientResponseException.class, () -> client.update(
            1,
            URI.create(URL),
            "",
            new Long[] {1L}
        ));
    }
}
