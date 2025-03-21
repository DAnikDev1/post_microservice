package src.danik.postservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentReadDto> findAllCommentsByPostId(@PathVariable @NotNull @Positive Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentReadDto createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.createComment(commentCreateDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Valid @Positive Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentReadDto updateExistedComment(@Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.updateComment(commentUpdateDto);
    }

}
