package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    void deleteById(UUID postId);
}
