package src.danik.postservice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import src.danik.postservice.dto.comment.CommentCreateDto;
import src.danik.postservice.dto.comment.CommentReadDto;
import src.danik.postservice.dto.comment.CommentUpdateDto;
import src.danik.postservice.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment getCommentById(long id);

    List<CommentReadDto> getCommentsByPostId(@NotNull @Positive Long postId);

    CommentReadDto createComment(CommentCreateDto commentCreateDto);

    void deleteComment(@Valid @Positive Long commentId);

    CommentReadDto updateComment(@Valid CommentUpdateDto commentUpdateDto);

    Comment saveComment(Comment comment);
}
