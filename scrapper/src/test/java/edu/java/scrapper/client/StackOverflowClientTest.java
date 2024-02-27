package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.dto.stackoverflow.QuestionResponse;
import java.time.OffsetDateTime;
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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class StackOverflowClientTest {

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @RegisterExtension
    private static final WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void stackOverflowClientProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.stackoverflow-api-url", wireMock::baseUrl);
    }

    @Test
    void fetchQuestion_ItemExist() {
        wireMock.stubFor(get("/questions/123?site=stackoverflow")
            .willReturn(ok()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "items": [
                            {
                                "title": "Title of question",
                                "last_activity_date": "2023-10-03T12:23:54Z"
                            }
                        ]
                    }
                    """)));

        QuestionResponse actual = stackOverflowClient.fetchQuestion(123).block();

        QuestionResponse expected = createQuestion("Title of question", "2023-10-03T12:23:54Z");
        assertEquals(expected, actual);
    }

    private QuestionResponse createQuestion(String title, String lastActivityDate) {
        var item = new QuestionResponse.Item(title, OffsetDateTime.parse(lastActivityDate));
        return new QuestionResponse(List.of(item));
    }

    @Test
    void fetchQuestion_NoItems_ReturnsMonoEmpty() {
        wireMock.stubFor(get("/questions/123?site=stackoverflow")
            .willReturn(ok()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "items": []
                    }
                    """)));

        Mono<QuestionResponse> result = stackOverflowClient.fetchQuestion(123);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void fetchQuestion_UnexpectedResponseCode_ExpectsError() {
        wireMock.stubFor(get("/questions/123?site=stackoverflow")
            .willReturn(aResponse().withStatus(418)));

        Mono<QuestionResponse> result = stackOverflowClient.fetchQuestion(123);

        StepVerifier.create(result)
            .expectError(WebClientResponseException.class)
            .verify();
    }
}
