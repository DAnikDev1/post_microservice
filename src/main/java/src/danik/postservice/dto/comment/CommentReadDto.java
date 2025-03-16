package src.danik.postservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentReadDto {
    private String content;
    private Long postId;
    private Long commentId;
    private Long userId;
    private List<Long> likeIds;

}
