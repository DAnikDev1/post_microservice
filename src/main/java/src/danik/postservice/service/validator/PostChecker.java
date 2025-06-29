package src.danik.postservice.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import src.danik.postservice.entity.Post;
import src.danik.postservice.exception.DataValidationException;
import src.danik.postservice.mapper.post.PostMapper;
import src.danik.postservice.service.impl.UserServiceImpl;

@Component
@RequiredArgsConstructor
public class PostChecker {
    private final UserServiceImpl userService;

    public void checkThatUserIsExist(Post post) {
        if (post.getUserId() != null && !userService.isUserExist(post.getUserId())) {
            throw new DataValidationException("User doesn't exist");
        }
        if (post.getUserId() == null) {
            throw new DataValidationException("Post have no owner");
        }
    }
    public void checkThatPostIsAlreadyDeleted(Post post) {
        if (post.isDeleted()) {
            throw new DataValidationException("Post is already deleted");
        }
    }

    public void checkThatPostIsPublished(Post post) {
        if (post.isPublished()) {
            throw new DataValidationException("Post is already published");
        }
    }
}
