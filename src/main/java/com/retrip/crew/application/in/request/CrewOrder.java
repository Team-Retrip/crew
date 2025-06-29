package com.retrip.crew.application.in.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrewOrder {
    DATE("createdAt");

    private final String field;
}
