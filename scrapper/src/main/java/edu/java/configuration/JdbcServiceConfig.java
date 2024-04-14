package edu.java.configuration;

import edu.java.domain.jdbc.dao.ChatDao;
import edu.java.domain.jdbc.dao.LinkChatDao;
import edu.java.domain.jdbc.dao.LinkDao;
import edu.java.jdbcservices.JdbcChatService;
import edu.java.jdbcservices.JdbcLinkChatService;
import edu.java.jdbcservices.JdbcLinkService;
import edu.java.services.ChatService;
import edu.java.services.LinkChatService;
import edu.java.services.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcServiceConfig {
    @Bean
    public ChatService jdbcChatService(
        LinkDao linkRepository,
        ChatDao chatRepository,
        LinkChatDao linkChatRepository
    ) {
        return new JdbcChatService(linkRepository, chatRepository, linkChatRepository);
    }

    @Bean
    public LinkService jdbcLinkService(
        LinkDao linkRepository,
        ChatDao chatRepository,
        LinkChatDao linkChatRepository
    ) {
        return new JdbcLinkService(chatRepository, linkRepository, linkChatRepository);
    }

    @Bean
    public LinkChatService jdbcLinkChatService(
        LinkChatDao linkChatRepository
    ) {
        return new JdbcLinkChatService(linkChatRepository);
    }
}
