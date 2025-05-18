package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.PostDeleteFailedException;
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

    public Post updatePost(UUID postId, String title, String content, UUID memberId) {
        Post post = findPost(postId);
        post.update(title, content, memberId);
        return post;
    }

    public void deletePost(UUID postId, CrewMember crewMember) {
        Post post = findPost(postId);
        if (!post.isDeletable(crewMember)) {
            throw new PostDeleteFailedException();
        }
        this.values.remove(post);
    }

    private Post findPost(UUID postId) {
        return this.values.stream().filter(p -> p.getId().equals(postId)).findAny()
                .orElseThrow(PostNotFoundException::new);
    }

    public void add(Post post) {
        this.values.add(post);
    }
}
