package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class ScrapperClientTest {

    @Autowired
    private ScrapperClient scrapperClient;

    @RegisterExtension
    private static final WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void botClientProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.scrapper-api-url", wireMock::baseUrl);
    }

    @Test
    void registerChat_Ok() {
        wireMock.stubFor(post("/tg-chat/1")
            .willReturn(ok()));

        Mono<Void> result = scrapperClient.registerChat(1L);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void registerChat_BadRequest_ExpectsError() {
        wireMock.stubFor(post("/tg-chat/1")
            .willReturn(badRequest()));

        Mono<Void> result = scrapperClient.registerChat(1L);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void registerChat_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(post("/tg-chat/1")
            .willReturn(aResponse().withStatus(418)));

        Mono<Void> result = scrapperClient.registerChat(1L);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void deleteChat_Ok() {
        wireMock.stubFor(delete("/tg-chat/1")
            .willReturn(ok()));

        Mono<Void> result = scrapperClient.deleteChat(1L);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void deleteChat_BadRequest_ExpectsError() {
        wireMock.stubFor(delete("/tg-chat/1")
            .willReturn(badRequest()));

        Mono<Void> result = scrapperClient.deleteChat(1L);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void deleteChat_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(delete("/tg-chat/1")
            .willReturn(aResponse().withStatus(418)));

        Mono<Void> result = scrapperClient.deleteChat(1L);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void getAllLinks_Ok() {
        var responseBody = """
            {
                "links": [
                    {
                        "id": 1,
                        "url": "https://example1.com"
                    },
                    {
                        "id": 2,
                        "url": "https://example2.com"
                    }
                ],
                "size": 2
            }
            """;
        wireMock.stubFor(get("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(ok()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        ListLinksResponse actual = scrapperClient.getAllLinks(1L).block();

        ListLinksResponse expected = ListLinksResponse.builder()
            .links(List.of(
                new LinkResponse(1L, "https://example1.com"),
                new LinkResponse(2L, "https://example2.com")
            ))
            .size(2)
            .build();
        assertEquals(expected, actual);
    }

    @Test
    void getAllLinks_BadRequest_ExpectsError() {
        wireMock.stubFor(get("/links")
            .willReturn(badRequest()));

        Mono<ListLinksResponse> result = scrapperClient.getAllLinks(1L);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void getAllLinks_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(get("/links")
            .willReturn(aResponse().withStatus(418)));

        Mono<ListLinksResponse> result = scrapperClient.getAllLinks(1L);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void addLink_Ok() {
        var requestBody = """
            {
                "link": "https://example.com"
            }
            """;
        var responseBody = """
            {
                "id": 2,
                "url": "https://example.com"
            }
            """;
        wireMock.stubFor(post("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson(requestBody))
            .willReturn(ok()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        AddLinkRequest dto = new AddLinkRequest("https://example.com");
        LinkResponse actual = scrapperClient.addLink(1L, dto).block();

        LinkResponse expected = new LinkResponse(2L, "https://example.com");
        assertEquals(expected, actual);
    }

    @Test
    void addLink_BadRequest_ExpectsError() {
        wireMock.stubFor(post("/links")
            .willReturn(badRequest()));

        Mono<LinkResponse> result = scrapperClient.addLink(1L, mock(AddLinkRequest.class));

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void addLink_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(post("/links")
            .willReturn(aResponse().withStatus(418)));

        Mono<LinkResponse> result = scrapperClient.addLink(1L, mock(AddLinkRequest.class));

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void deleteLink_Ok() {
        var requestBody = """
            {
                "link": "https://example.com"
            }
            """;
        var responseBody = """
            {
                "id": 2,
                "url": "https://example.com"
            }
            """;
        wireMock.stubFor(delete("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson(requestBody))
            .willReturn(ok()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        RemoveLinkRequest dto = new RemoveLinkRequest("https://example.com");
        LinkResponse actual = scrapperClient.deleteLink(1L, dto).block();

        LinkResponse expected = new LinkResponse(2L, "https://example.com");
        assertEquals(expected, actual);
    }

    @Test
    void deleteLink_BadRequest_ExpectsError() {
        wireMock.stubFor(delete("/links")
            .willReturn(badRequest()));

        Mono<LinkResponse> result = scrapperClient.deleteLink(1L, mock(RemoveLinkRequest.class));

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void deleteLink_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(delete("/links")
            .willReturn(aResponse().withStatus(418)));

        Mono<LinkResponse> result = scrapperClient.deleteLink(1L, mock(RemoveLinkRequest.class));

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }
}
