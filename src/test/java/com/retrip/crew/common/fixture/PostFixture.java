package com.retrip.crew.common.fixture;

import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.UpdatePostRequest;

import java.util.UUID;

public class PostFixture {
    public static CreatePostRequest createRequest(String title, String content) {
        return new CreatePostRequest(
                title,
                content
        );
    }
    public static UpdatePostRequest updateRequest(String title, String content, UUID userId) {
        return new UpdatePostRequest(
                title,
                content,
                userId
        );
    }
}
