package src.danik.postservice.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.exception.NoAccessException;
import src.danik.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Tag(name = "System API", description = "API for other microservices. Requires secret header")
@RequestMapping("/api/v1/internal")
@Slf4j
public class SystemController {
    private final PostService postService;
    @Value("${system.key}")
    private String secretSystemKey;

    @GetMapping("/popular_posts")
    @Operation(summary = "Get top 100 popular posts and return ids of authors these posts")
    @ResponseStatus(HttpStatus.OK)
    public Set<Long> getUserIdsFromPopularPosts(@RequestHeader("system-key") String systemKey) {
        if (!systemKey.equals(secretSystemKey)) {
            throw new NoAccessException("Wrong system key");
        }
        return postService.findAuthorOfPopularPosts(100);
    }
}
