package src.danik.postservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.dto.post.PostUpdateDto;
import src.danik.postservice.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostReadDto getPostById(@PathVariable @Valid @Positive Long postId) {
        return postService.getPostReadDtoById(postId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostReadDto createPost(@RequestBody @NotNull @Valid PostCreateDto postCreateDto) {
        return postService.createPost(postCreateDto);
    }
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable @Valid @Positive Long postId) {
        postService.deletePostById(postId);
    }
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostReadDto updatePost(@PathVariable @Valid Long postId, @RequestBody @NotNull @Valid PostUpdateDto postUpdateDto) {
        return postService.updatePost(postId, postUpdateDto);
    }
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostReadDto publishPost(@PathVariable @Valid @Positive Long postId) {
        return postService.publishPost(postId);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostReadDto> getAllPosts() {
        return postService.getAllPosts();
    }

}
