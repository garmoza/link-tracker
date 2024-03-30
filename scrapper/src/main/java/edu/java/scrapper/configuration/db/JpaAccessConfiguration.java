package edu.java.scrapper.configuration.db;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.repository.jpa.SubscriptionRepository;
import edu.java.scrapper.repository.jpa.TgChatRepository;
import edu.java.scrapper.repository.jpa.TrackableLinkRepository;
import edu.java.scrapper.service.SubscriptionService;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.UpdateService;
import edu.java.scrapper.service.impl.jpa.JpaSubscriptionService;
import edu.java.scrapper.service.impl.jpa.JpaTgChatService;
import edu.java.scrapper.service.impl.jpa.JpaUpdateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public TgChatService tgChatService(TgChatRepository tgChatRepository) {
        return new JpaTgChatService(tgChatRepository);
    }

    @Bean
    public SubscriptionService subscriptionService(
        SubscriptionRepository subscriptionRepository,
        TgChatRepository tgChatRepository,
        TrackableLinkRepository trackableLinkRepository
    ) {
        return new JpaSubscriptionService(subscriptionRepository, tgChatRepository, trackableLinkRepository);
    }

    @Bean
    public UpdateService updateService(
        TrackableLinkRepository trackableLinkRepository,
        SubscriptionRepository subscriptionRepository,
        BotClient botClient
    ) {
        return new JpaUpdateService(trackableLinkRepository, subscriptionRepository, botClient);
    }
}
