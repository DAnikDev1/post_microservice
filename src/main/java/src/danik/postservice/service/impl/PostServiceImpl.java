package src.danik.postservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.dto.post.PostUpdateDto;
import src.danik.postservice.entity.Post;
import src.danik.postservice.kafka.event.notifications.PostDeletedEvent;
import src.danik.postservice.kafka.event.notifications.PostPublishedEvent;
import src.danik.postservice.mapper.post.PostMapper;
import src.danik.postservice.repository.PostRepository;
import src.danik.postservice.service.PostService;
import src.danik.postservice.service.validator.PostChecker;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostChecker postChecker;

    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Post getPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id = %d was not found", id)));
    }

    @Override
    public PostReadDto getPostReadDtoById(Long id) {
        PostReadDto postDto = getPostFromCache("popularPosts", id);
        if (postDto != null) return postDto;

        postDto = getPostFromCache("posts", id);
        if (postDto != null) return postDto;

        Post post = getPostById(id);

        postDto = postMapper.toReadDto(post);
        putPostInCache("posts", id, postDto);

        return postDto;
    }

    @Override
    @Transactional
    public PostReadDto createPost(PostCreateDto postCreateDto) {
        Post post = postMapper.toEntity(postCreateDto);
        postChecker.checkThatUserIsExist(post);

        Post result = postRepository.save(post);
        return postMapper.toReadDto(result);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict("posts"),
            @CacheEvict(value = "popularPosts", allEntries = true)
    })
    public void deletePostById(Long postId) {
        Post post = getPostById(postId);
        postChecker.checkThatPostIsAlreadyDeleted(post);
        post.setDeleted(true);

        applicationEventPublisher.publishEvent(new PostDeletedEvent(
                post.getUserId(),
                post.getId()));
        postRepository.save(post);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "posts", key = "#postId"),
            @CacheEvict(value = "popularPosts", key = "#postId")
    })
    public PostReadDto updatePost(Long postId, PostUpdateDto postUpdateDto) {
        Post post = getPostById(postId);
        post.setContent(postUpdateDto.getContent());
        Post saved = postRepository.save(post);

        return postMapper.toReadDto(saved);
    }

    @Override
    @Transactional
    @CachePut(value = "posts", key = "#postId")
    public PostReadDto publishPost(Long postId) {
        Post post = getPostById(postId);

        postChecker.checkThatPostIsPublished(post);
        postChecker.checkThatPostIsAlreadyDeleted(post);
        postChecker.checkThatUserIsExist(post);

        post.setPublished(true);
        Post result = postRepository.save(post);

        applicationEventPublisher.publishEvent(new PostPublishedEvent(
                result.getUserId(),
                result.getId()));
        return postMapper.toReadDto(result);
    }

    @Override
    public List<PostReadDto> getAllPosts() {
        return postRepository.findAll().stream().map(postMapper::toReadDto).toList();
    }

    @Override
    public List<Post> findPopularPosts(int postsCount) {
        List<Post> posts = postRepository.findPopularPosts(PageRequest.of(0, postsCount));
        posts.forEach(post -> putPostInCache("popularPosts", post.getId(), postMapper.toReadDto(post)));

        return posts;
    }

    @Override
    public Set<Long> findAuthorOfPopularPosts(int postsCount) {
        List<Post> posts = findPopularPosts(postsCount);
        return posts.stream()
                .map(Post::getUserId)
                .collect(Collectors.toSet());
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    private PostReadDto getPostFromCache(String cacheName, Long id) {
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .map(cache -> cache.get(id, PostReadDto.class))
                .orElse(null);
    }

    private void putPostInCache(String cacheName, Long id, PostReadDto dto) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(cache -> cache.put(id, dto));
    }
}
