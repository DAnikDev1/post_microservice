package src.danik.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import src.danik.postservice.entity.Like;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByCommentIdAndUserId(Long commentId, Long userId);

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    @Modifying
    void deleteByPostIdAndUserId(long postId, long userId);

    @Modifying
    void deleteByCommentIdAndUserId(long commentId, long userId);
}
