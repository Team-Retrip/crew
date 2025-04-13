package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.PostDeleteException;
import com.retrip.crew.domain.exception.PostNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.UUID;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class Posts {
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> values = new ArrayList<>();

    public Posts() {
        this.values = createEmptyValues();
    }

    private List<Post> createEmptyValues() {
        return new ArrayList<>();
    }

    public Post updatePost(UUID postId, String title, String content, UUID userId) {
        Post post = this.values.stream().filter(p -> p.getId().equals(postId)).findAny()
                .orElseThrow(PostNotFoundException::new);
        post.update(title, content, userId);
        return post;
    }

    public UUID deletePost(UUID postId, CrewMember crewMember) {
        Post post = this.values.stream().filter(p -> p.getId().equals(postId)).findAny()
                .orElseThrow(PostNotFoundException::new);
        if (!post.isDeletable(crewMember)) {
            throw new PostDeleteException();
        }
        this.values.remove(post);
        return post.getId();
    }
}
