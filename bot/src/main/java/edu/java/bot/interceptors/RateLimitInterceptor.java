package edu.java.bot.interceptors;

import edu.java.bot.exceptions.TooMuchRequestsException;
import edu.java.bot.services.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String apiKey = request.getHeader("X-api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }

        Bucket tokenBucket = rateLimitService.resolveBucket(apiKey);
        if (tokenBucket.tryConsume(1)) {
            return true;
        } else {
            throw new TooMuchRequestsException();
        }
    }
}
