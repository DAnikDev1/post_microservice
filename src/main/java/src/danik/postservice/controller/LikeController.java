package src.danik.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.repository.types.LikeType;
import src.danik.postservice.service.LikeService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Like API", description = "API for managing Likes")
@RequestMapping("/api/v1/likes")
@Slf4j
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add Like from User to Post")
    public LikeDto userLikePost(@Valid @RequestBody LikeDto likeDto) {
        log.info("User like the post with id: {}", likeDto);
        return likeService.userLike(likeDto, LikeType.POST);
    }

    @DeleteMapping("/post/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove Like from Post using Id of Like and Id of element, where our Like is set")
    public void userRemoveLikeFromPost(@PathVariable @NotNull @Positive Long likeId, @Valid @RequestBody LikeDto likeDto) {
        log.info("User is removing like from post using likeId = {} and id of post = {}", likeId, likeDto);
        likeService.removeLike(likeId, LikeType.POST, likeDto);
    }

    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add Like from User to Comment")
    public LikeDto userLikeComment(@Valid @RequestBody LikeDto likeDto) {
        log.info("User like the comment with id: {}", likeDto);
        return likeService.userLike(likeDto, LikeType.COMMENT);
    }

    @DeleteMapping("/comment/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove Like from Comment using Id of Like and Id of element, where our Like is set")
    public void userRemoveLikeFromComment(@PathVariable @NotNull @Positive Long likeId, @Valid @RequestBody LikeDto likeDto) {
        log.info("User is removing like from comment using likeId = {} and id of comment = {}", likeId, likeDto);
        likeService.removeLike(likeId, LikeType.COMMENT, likeDto);
    }

}
