package com.retrip.crew.application.in.request.crew;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrewOrder {
    DATE("createdAt");

    private final String field;
}
