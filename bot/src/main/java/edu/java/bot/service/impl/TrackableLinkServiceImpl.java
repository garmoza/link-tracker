package edu.java.bot.service.impl;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.entity.Link;
import edu.java.bot.service.TrackableLinkService;
import edu.java.bot.source.TrackableLink;
import edu.java.model.request.AddLinkRequest;
import edu.java.model.request.RemoveLinkRequest;
import edu.java.model.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class TrackableLinkServiceImpl implements TrackableLinkService {

    private final List<TrackableLink> trackableLinks;
    private final ScrapperClient scrapperClient;

    @Override
    public List<String> getTrackableLinks(long chatId) {
        ListLinksResponse response = scrapperClient.getAllLinks(chatId).block();
        return response.links().stream()
            .map(linkResponse -> linkResponse.url().toString())
            .toList();
    }

    @Override
    public boolean isTrackableLink(String link) {
        for (var trackableLink : trackableLinks) {
            URI url = URI.create(link);
            if (trackableLink.supports(new Link(url.toString(), url.getHost()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean subscribe(long chatId, String link) {
        try {
            scrapperClient.addLink(chatId, new AddLinkRequest(link)).block();
            return true;
        } catch (WebClientResponseException e) {
            return false;
        }
    }

    @Override
    public boolean unsubscribe(long chatId, String link) {
        try {
            scrapperClient.deleteLink(chatId, new RemoveLinkRequest(link)).block();
            return true;
        } catch (WebClientResponseException e) {
            return false;
        }
    }
}
