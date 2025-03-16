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
    public ResponseEntity<LikeDto> userLikePost(@Valid @RequestBody LikeDto likeDto) {
        LikeDto answer = likeService.userLike(likeDto, LikeType.POST);
        return ResponseEntity.status(HttpStatus.CREATED).body(answer);
    }

    @DeleteMapping("/post/{likeId}")
    public ResponseEntity<Void> userRemoveLikeFromPost(@PathVariable @NotNull @Positive Long likeId, @Valid @RequestBody LikeDto likeDto) {
        likeService.removeLike(likeId, LikeType.POST, likeDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/comment")
    public ResponseEntity<LikeDto> userLikeComment(@Valid @RequestBody LikeDto likeDto) {
        LikeDto answer = likeService.userLike(likeDto, LikeType.COMMENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(answer);
    }

    @DeleteMapping("/comment/{likeId}")
    public ResponseEntity<Void> userRemoveLikeFromComment(@PathVariable @NotNull @Positive Long likeId, @Valid @RequestBody LikeDto likeDto) {
        likeService.removeLike(likeId, LikeType.COMMENT, likeDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
