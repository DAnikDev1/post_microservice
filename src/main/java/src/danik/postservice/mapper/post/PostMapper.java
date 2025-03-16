package src.danik.postservice.mapper.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.entity.Like;
import src.danik.postservice.entity.Post;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    Post toEntity(PostCreateDto postCreateDto);

    Post toEntity(PostReadDto postReadDto);

    @Mapping(source = "likes", target = "countOfLikes", qualifiedByName = "countLikes")
    PostReadDto toReadDto(Post post);

    @Named("countLikes")
    default Integer countLikes(List<Like> likes) {
        if (likes == null) return 0;
        return likes.size();
    }
}
