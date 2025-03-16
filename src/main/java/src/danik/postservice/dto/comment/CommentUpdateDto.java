package src.danik.postservice.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentUpdateDto {
    @NotNull
    @Positive
    private Long commentId;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 2048)
    private String content;
}
