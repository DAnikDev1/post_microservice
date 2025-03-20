package src.danik.postservice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.dto.post.PostUpdateDto;

import java.util.List;

public interface PostService {
    PostReadDto getPostReadDtoById(@Valid Long postId);

    PostReadDto createPost(@NotNull @Valid PostCreateDto postCreateDto);

    void deletePostById(@Valid Long postId);

    PostReadDto updatePost(@PathVariable @Valid Long postId, @NotNull @Valid PostUpdateDto postUpdateDto);

    PostReadDto publishPost(@Valid @Positive Long postId);

    List<PostReadDto> getAllPosts();
}
