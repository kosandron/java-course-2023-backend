package edu.java.configuration;

import edu.java.domain.jpa.dao.JpaChatDao;
import edu.java.domain.jpa.dao.JpaLinksDao;
import edu.java.jpaservices.JpaChatService;
import edu.java.jpaservices.JpaLinkChatService;
import edu.java.jpaservices.JpaLinkService;
import edu.java.services.ChatService;
import edu.java.services.LinkChatService;
import edu.java.services.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaServiceConfig {
    @Bean
    public LinkService jpaLinkService(
        JpaChatDao chatRepository,
        JpaLinksDao linkRepository
    ) {
        return new JpaLinkService(
            chatRepository,
            linkRepository
        );
    }

    @Bean
    public ChatService jpaTgChatService(
        JpaChatDao chatRepository,
        JpaLinksDao linkRepository
    ) {
        return new JpaChatService(
            chatRepository,
            linkRepository
        );
    }

    @Bean
    public LinkChatService jpaLinkChatService(
        JpaChatDao chatRepository,
        JpaLinksDao linkRepository
    ) {
        return new JpaLinkChatService(
            chatRepository,
            linkRepository
        );
    }
}
