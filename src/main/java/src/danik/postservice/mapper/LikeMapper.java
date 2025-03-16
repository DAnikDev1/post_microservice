package src.danik.postservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.entity.Like;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    Like toEntity(LikeDto likeDto);

    @Mapping(source = "post.id", target = "likeId")
    LikeDto toLikePostDto(Like like);

    @Mapping(source = "comment.id", target = "likeId")
    LikeDto toLikeCommentDto(Like like);
}
