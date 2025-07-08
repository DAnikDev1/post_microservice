package src.danik.postservice.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import src.danik.postservice.service.PostService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheScheduler {
    private final PostService postService;
    private final CacheManager cacheManager;
    
    @Value("${popular.posts.amount:100}")
    private int popularPostsCount;

    @Scheduled(fixedDelay = 600000)
    public void generatePopularPostsCache() {
        log.info("Generating hot cache for popular posts");
        try {
            Cache cache = cacheManager.getCache("popularPosts");
            if (cache != null) {
                cache.clear();
            }
            
            postService.findPopularPosts(popularPostsCount);
            
            log.info("{} posts was cached", popularPostsCount);
        } catch (Exception e) {
            log.error("Error to generate hot cache", e);
        }
    }
}
