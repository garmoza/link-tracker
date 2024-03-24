package edu.java.scrapper.configuration.db;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.repository.jdbc.SubscriptionRepository;
import edu.java.scrapper.repository.jdbc.TgChatRepository;
import edu.java.scrapper.repository.jdbc.TrackableLinkRepository;
import edu.java.scrapper.service.SubscriptionService;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.UpdateService;
import edu.java.scrapper.service.impl.jdbc.JdbcSubscriptionService;
import edu.java.scrapper.service.impl.jdbc.JdbcTgChatService;
import edu.java.scrapper.service.impl.jdbc.JdbcUpdateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public TgChatService tgChatService(TgChatRepository tgChatRepository) {
        return new JdbcTgChatService(tgChatRepository);
    }

    @Bean
    public SubscriptionService subscriptionService(
        SubscriptionRepository subscriptionRepository,
        TgChatRepository tgChatRepository,
        TrackableLinkRepository trackableLinkRepository
    ) {
        return new JdbcSubscriptionService(subscriptionRepository, tgChatRepository, trackableLinkRepository);
    }

    @Bean
    public UpdateService updateService(
        TrackableLinkRepository trackableLinkRepository,
        SubscriptionRepository subscriptionRepository,
        BotClient botClient
    ) {
        return new JdbcUpdateService(trackableLinkRepository, subscriptionRepository, botClient);
    }
}
