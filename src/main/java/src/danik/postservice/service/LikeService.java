package src.danik.postservice.service;

import jakarta.validation.Valid;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.repository.types.LikeType;

public interface LikeService {
    LikeDto userLike(@Valid LikeDto likeDto, LikeType likeType);
    void removeLike(Long likeId, LikeType likeType, LikeDto likeDto);
}

