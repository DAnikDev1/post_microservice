package src.danik.postservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.dto.post.PostUpdateDto;
import src.danik.postservice.entity.Post;
import src.danik.postservice.mapper.post.PostMapper;
import src.danik.postservice.repository.PostRepository;
import src.danik.postservice.service.PostService;
import src.danik.postservice.service.validator.PostChecker;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostChecker postChecker;

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
        post.setPublished(false); // Защита от пользователя, пост не может быть уже опубликованным

        Post result = postRepository.save(post);
        return postMapper.toReadDto(result);
    }

    @Override
    @Transactional
    public void deletePostById(Long postId) {
        Post post = getPostById(postId);
        postChecker.checkThatPostIsAlreadyDeleted(post);
        post.setDeleted(true);

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

        postChecker.checkThatPostIsNotPublished(post);
        postChecker.checkThatPostIsAlreadyDeleted(post);
        postChecker.checkThatUserIsExist(post);

        post.setPublished(true);
        Post result = postRepository.save(post);
        return postMapper.toReadDto(result);
    }
}
