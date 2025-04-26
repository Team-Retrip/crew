package com.retrip.crew.application.in.request.demand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DemandOrder {
    DATE("createdAt");

    private final String field;
}
