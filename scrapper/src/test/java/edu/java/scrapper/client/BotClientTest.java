package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.dto.request.LinkUpdate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class BotClientTest {

    @Autowired
    private BotClient botClient;

    @RegisterExtension
    private static final WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void botClientProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.bot-api-url", wireMock::baseUrl);
    }

    @Test
    void sendUpdate_Ok() {
        var requestBody = """
            {
                "id": 1,
                "url": "https://example.com",
                "description": "Description",
                "tgChatIds": [2, 3]
            }
            """;
        wireMock.stubFor(post("/updates")
            .withRequestBody(equalToJson(requestBody))
            .willReturn(ok()));

        LinkUpdate dto = new LinkUpdate(1L, "https://example.com", "Description", List.of(2L, 3L));
        Mono<Void> result = botClient.sendUpdate(dto);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void sendUpdate_BadRequest_ExpectsError() {
        wireMock.stubFor(post("/updates")
            .willReturn(badRequest()));

        Mono<Void> result = botClient.sendUpdate(mock(LinkUpdate.class));

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }

    @Test
    void sendUpdate_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(post("/updates")
            .willReturn(aResponse().withStatus(418)));

        Mono<Void> result = botClient.sendUpdate(mock(LinkUpdate.class));

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }
}
