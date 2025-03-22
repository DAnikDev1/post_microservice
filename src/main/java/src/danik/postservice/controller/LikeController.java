package src.danik.postservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.repository.types.LikeType;
import src.danik.postservice.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto userLikePost(@Valid @RequestBody LikeDto likeDto) {
        return likeService.userLike(likeDto, LikeType.POST);
    }

    @DeleteMapping("/post/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userRemoveLikeFromPost(@PathVariable @NotNull @Positive Long likeId, @Valid @RequestBody LikeDto likeDto) {
        likeService.removeLike(likeId, LikeType.POST, likeDto);
    }

    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto userLikeComment(@Valid @RequestBody LikeDto likeDto) {
        return likeService.userLike(likeDto, LikeType.COMMENT);
    }

    @DeleteMapping("/comment/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userRemoveLikeFromComment(@PathVariable @NotNull @Positive Long likeId, @Valid @RequestBody LikeDto likeDto) {
        likeService.removeLike(likeId, LikeType.COMMENT, likeDto);
    }

}
