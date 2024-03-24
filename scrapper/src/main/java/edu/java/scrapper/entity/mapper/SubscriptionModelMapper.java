package edu.java.scrapper.entity.mapper;

import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import edu.java.scrapper.entity.Subscription;
import java.net.URI;
import java.util.List;

public class SubscriptionModelMapper {

    private SubscriptionModelMapper() {
    }

    public static LinkResponse toLinkResponse(Subscription entity) {
        if (entity == null) {
            return null;
        }

        return LinkResponse.builder()
            .id(entity.getId().getChatId())
            .url(URI.create(entity.getId().getLinkUrl()))
            .build();
    }

    public static ListLinksResponse toListLinksResponse(List<Subscription> entities) {
        List<LinkResponse> links = entities.stream()
            .map(SubscriptionModelMapper::toLinkResponse)
            .toList();

        return ListLinksResponse.builder()
            .links(links)
            .size(links.size())
            .build();
    }
}
