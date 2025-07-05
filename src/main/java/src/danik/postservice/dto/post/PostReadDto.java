package src.danik.postservice.dto.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostReadDto {
    private Long userId;
    private String content;
    private Long id;
    private Integer likesCount;
    private Boolean deleted;
    private Boolean published;
    private LocalDateTime createdAt;
}
