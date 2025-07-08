package src.danik.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.dto.post.PostUpdateDto;
import src.danik.postservice.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post API", description = "API for managing Posts")
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    @Operation(summary = "Get post using id")
    @ResponseStatus(HttpStatus.OK)
    public PostReadDto getPostById(@PathVariable @Valid @Positive Long postId) {
        return postService.getPostReadDtoById(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new post and return it")
    public PostReadDto createPost(@RequestBody @NotNull @Valid PostCreateDto postCreateDto) {
        log.info("Creating new post: {}", postCreateDto);
        return postService.createPost(postCreateDto);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete post using id")
    public void deletePostById(@PathVariable @Valid @Positive Long postId) {
        log.info("Deleting post with id = {}", postId);
        postService.deletePostById(postId);
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update post using id and postUpdateDto")
    public PostReadDto updatePost(@PathVariable @Valid Long postId, @RequestBody @NotNull @Valid PostUpdateDto postUpdateDto) {
        log.info("Updating post using postId = {} and new post: {}", postId, postUpdateDto);
        return postService.updatePost(postId, postUpdateDto);
    }

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set post published to true")
    public PostReadDto publishPost(@PathVariable @Valid @Positive Long postId) {
        log.info("Publishing post to public with id = {}", postId);
        return postService.publishPost(postId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get All existed posts")
    public List<PostReadDto> getAllPosts() {
        log.info("Trying to get all existed posts");
        return postService.getAllPosts();
    }

}
