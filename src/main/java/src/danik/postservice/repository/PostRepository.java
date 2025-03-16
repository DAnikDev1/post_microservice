package src.danik.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import src.danik.postservice.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(long userId);

    @Query("SELECT post FROM Post post LEFT JOIN FETCH post.likes WHERE post.userId = :userId")
    List<Post> findByUserIdWithLikes(long userId);
}
