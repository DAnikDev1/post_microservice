package src.danik.postservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record LikeDto (@NotNull @Positive Long likeId) {

}
