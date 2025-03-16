package src.danik.postservice.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import src.danik.postservice.dto.comment.CommentCreateDto;
import src.danik.postservice.dto.comment.CommentReadDto;
import src.danik.postservice.dto.comment.CommentUpdateDto;
import src.danik.postservice.entity.Comment;
import src.danik.postservice.entity.Like;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment toEntity(CommentReadDto commentReadDto);

    Comment toEntity(CommentCreateDto commentCreateDto);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "likes", target = "likeIds", qualifiedByName = "getListOfIdFromListOfLikes",
            conditionExpression = "java(comment.getLikes() != null)")
    CommentReadDto toReadDto(Comment comment);

    Comment toUpdate(@MappingTarget Comment comment, CommentUpdateDto dto);

    default List<Long> getListOfIdFromListOfLikes(List<Like> likes) {
        return likes.stream().map(Like::getId).toList();
    }
}
