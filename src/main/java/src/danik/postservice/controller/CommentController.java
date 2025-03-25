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
import src.danik.postservice.dto.comment.CommentCreateDto;
import src.danik.postservice.dto.comment.CommentReadDto;
import src.danik.postservice.dto.comment.CommentUpdateDto;
import src.danik.postservice.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment API", description = "API for managing comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of comments using Id of Post")
    public List<CommentReadDto> findAllCommentsByPostId(@PathVariable @NotNull @Positive Long postId) {
        log.info("Finding all comments by post id = {}", postId);
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new comment and return it")
    public CommentReadDto createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("Creating new comment: {}", commentCreateDto);
        return commentService.createComment(commentCreateDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete comment using Id")
    public void deleteComment(@PathVariable @Valid @Positive Long commentId) {
        log.info("Deleting comment with id = {}", commentId);
        commentService.deleteComment(commentId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update existed comment using commentUpdateDto and return updated comment")
    public CommentReadDto updateExistedComment(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        log.info("Updating comment using: {}", commentUpdateDto);
        return commentService.updateComment(commentUpdateDto);
    }

}
