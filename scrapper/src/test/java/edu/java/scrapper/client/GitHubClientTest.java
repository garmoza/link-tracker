package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.dto.github.RepositoryResponse;
import java.time.OffsetDateTime;
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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class GitHubClientTest {

    @Autowired
    private GitHubClient gitHubClient;

    @RegisterExtension
    private static final WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void gitHubClientProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.git-hub-api-url", wireMock::baseUrl);
    }

    @Test
    void fetchRepository_Ok() {
        wireMock.stubFor(get("/repos/test-user/test-repo")
            .willReturn(ok()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "name": "test-repo",
                        "updated_at": "2024-02-22T20:38:57Z",
                        "pushed_at": "2024-02-22T20:45:55Z"
                    }
                    """)));

        RepositoryResponse actual = gitHubClient.fetchRepository("test-user", "test-repo").block();

        RepositoryResponse expected = RepositoryResponse.builder()
            .name("test-repo")
            .updatedAt(OffsetDateTime.parse("2024-02-22T20:38:57Z"))
            .pushedAt(OffsetDateTime.parse("2024-02-22T20:45:55Z"))
            .build();
        assertEquals(expected, actual);
    }

    @Test
    void fetchRepository_NotFound_ReturnsMonoEmpty() {
        wireMock.stubFor(get("/repos/test-user/test-repo")
            .willReturn(notFound()));

        Mono<RepositoryResponse> result = gitHubClient.fetchRepository("test-user", "test-repo");

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void fetchRepository_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(get("/repos/test-user/test-repo")
            .willReturn(aResponse().withStatus(418)));

        Mono<RepositoryResponse> result = gitHubClient.fetchRepository("test-user", "test-repo");

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }
}
