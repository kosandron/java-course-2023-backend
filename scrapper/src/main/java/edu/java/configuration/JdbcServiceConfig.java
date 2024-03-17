package edu.java.configuration;

import edu.java.domain.dao.ChatDao;
import edu.java.domain.dao.LinkChatDao;
import edu.java.domain.dao.LinkDao;
import edu.java.jdbcservices.JdbcChatService;
import edu.java.jdbcservices.JdbcLinkChatService;
import edu.java.jdbcservices.JdbcLinkService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcServiceConfig {
    @Bean
    public JdbcChatService jdbcChatService(
        LinkDao linkRepository,
        ChatDao chatRepository,
        LinkChatDao linkChatRepository
    ) {
        return new JdbcChatService(linkRepository, chatRepository, linkChatRepository);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(
        LinkDao linkRepository,
        ChatDao chatRepository,
        LinkChatDao linkChatRepository
    ) {
        return new JdbcLinkService(chatRepository, linkRepository, linkChatRepository);
    }

    @Bean
    public JdbcLinkChatService jdbcLinkChatService(
        LinkChatDao linkChatRepository
    ) {
        return new JdbcLinkChatService(linkChatRepository);
    }
}
