package src.danik.postservice.mapper.post;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.entity.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    Post toEntity(PostCreateDto postCreateDto);

    Post toEntity(PostReadDto postReadDto);

    PostReadDto toReadDto(Post post);

}
