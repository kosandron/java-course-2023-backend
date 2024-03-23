package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 1234)
public class ScrapperTgChatHttpClientTests {
    private final ScrapperTgChatHttpClient client = new ScrapperTgChatHttpClient("http://localhost:1234");

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
}
