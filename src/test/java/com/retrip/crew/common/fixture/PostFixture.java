package com.retrip.crew.common.fixture;

import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.UpdatePostRequest;

import java.util.UUID;

public class PostFixture {
    public static UUID MEMBER_ID = UUID.fromString("13c8ab91-76bc-4f70-93e9-89f1a65dc640");

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
