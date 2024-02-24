package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubWebClient() {
        return new GitHubWebClient();
    }

    @Bean
    public StackOverflowClient stackOverflowWebClient() {
        return new StackOverflowWebClient();
    }
}
