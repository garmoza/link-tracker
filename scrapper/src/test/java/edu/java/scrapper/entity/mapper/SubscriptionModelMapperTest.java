package edu.java.scrapper.entity.mapper;

import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.entity.Subscription;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubscriptionModelMapperTest {

    @Test
    void toLinkResponse() {
        Subscription entity = Subscription.builder()
            .chatId(1L)
            .linkUrl("https://example.com")
            .lastUpdate(OffsetDateTime.parse("2024-03-17T12:00:00Z"))
            .build();

        LinkResponse actual = SubscriptionModelMapper.toLinkResponse(entity);

        LinkResponse expected = LinkResponse.builder()
            .id(1L)
            .url(URI.create("https://example.com"))
            .build();
        assertEquals(expected, actual);
    }

    @Test
    void toLinkResponse_EntityIsNull() {
        LinkResponse actual = SubscriptionModelMapper.toLinkResponse(null);

        assertNull(actual);
    }

    @Test
    void toListLinksResponse() {
        List<Subscription> entities = List.of(
            new Subscription(1L, "https://example1.com", OffsetDateTime.parse("2024-03-17T12:00:00Z")),
            new Subscription(2L, "https://example2.com", OffsetDateTime.parse("2024-03-17T13:00:00Z"))
        );

        ListLinksResponse actual = SubscriptionModelMapper.toListLinksResponse(entities);

        var links = List.of(
            new LinkResponse(1L, URI.create("https://example1.com")),
            new LinkResponse(2L, URI.create("https://example2.com"))
        );
        ListLinksResponse expected = ListLinksResponse.builder()
            .links(links)
            .size(2)
            .build();
        assertEquals(expected, actual);
    }
}
