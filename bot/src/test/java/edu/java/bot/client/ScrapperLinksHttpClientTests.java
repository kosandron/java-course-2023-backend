package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.dto.LinkResponse;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonSchema;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 1234)
public class ScrapperLinksHttpClientTests {
    private static final String URL1 = "https://github.com/kosandron/java-course-2023";
    private static final String URL2 = "https://github.com/kosandron/java-course-2023-backend";

    private final ScrapperLinksHttpClient client = new ScrapperLinksHttpClient("http://localhost:1234");

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
}
