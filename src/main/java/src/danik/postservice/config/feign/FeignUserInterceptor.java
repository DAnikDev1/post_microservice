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
        requestTemplate.header("social-user-id", String.valueOf(userContext.getUserIdVault()));
    }
}
