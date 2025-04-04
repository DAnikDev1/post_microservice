package src.danik.postservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
import src.danik.postservice.service.listener.NotificationEventListener;
import src.danik.postservice.service.producer.NotificationProducer;
import src.danik.postservice.service.validator.PostChecker;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostChecker postChecker;

    private final ApplicationEventPublisher applicationEventPublisher;

    public Post getPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id = %d was not found", id)));
    }

    @Override
    public PostReadDto getPostReadDtoById(Long postId) {
        return postMapper.toReadDto(getPostById(postId));
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
    public PostReadDto updatePost(Long postId, PostUpdateDto postUpdateDto) {
        Post post = getPostById(postId);
        post.setContent(postUpdateDto.getContent());
        Post saved = postRepository.save(post);

        return postMapper.toReadDto(saved);
    }

    @Override
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
}
