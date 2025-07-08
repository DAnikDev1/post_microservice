package src.danik.postservice.config.user;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import src.danik.postservice.exception.NoAccessException;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class UserHeaderFilter implements Filter {
    private final UserContext userContext;
    @Value("${system.key}")
    private String secretSystemKey;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String userId = httpServletRequest.getHeader("social-user-id");
        String systemKey = httpServletRequest.getHeader("system-key");
        if (userId != null && !userId.isEmpty()) {
            try {
                userContext.setUserIdVault(Long.parseLong(userId));
            } catch (NumberFormatException e) {
                if (systemKey != null && !systemKey.isEmpty() && systemKey.equals(secretSystemKey)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    throw new NoAccessException("Request without social user id");
                }
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            userContext.clearThreadLocal();
        }
    }
}
