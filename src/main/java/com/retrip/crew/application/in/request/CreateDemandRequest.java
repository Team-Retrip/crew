package com.retrip.crew.application.in.request;

import java.util.UUID;

public record CreateDemandRequest(
        UUID memberId
) {
}
