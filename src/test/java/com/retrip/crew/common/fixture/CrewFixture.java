package com.retrip.crew.common.fixture;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CrewFixture {

    public static CrewCreateRequest createCrewRequest(UUID memberId, String title, String description, int maxMembers) {
        return new CrewCreateRequest(
                memberId,
                title,
                description,
                maxMembers
        );
    }

    public static List<CrewCreateRequest> createMultipleCrews(int count, UUID memberId, String baseTitle, String baseDescription, int maxMembers) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    String title = baseTitle + " " + (i + 1);
                    String description = baseDescription + " " + (i + 1);
                    return createCrewRequest(memberId, title, description, maxMembers);
                })
                .collect(Collectors.toList());
    }
}
