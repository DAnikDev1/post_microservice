package src.danik.postservice.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import src.danik.postservice.config.user.UserContext;


@RequiredArgsConstructor
public class FeignUserInterceptor implements RequestInterceptor {
    private final UserContext userContext;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = userContext.getUserIdVault();
        if(userId != null) {
            requestTemplate.header("social-user-id", userId.toString());
        }
    }
}
