package src.danik.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.danik.postservice.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(long postId);
}
