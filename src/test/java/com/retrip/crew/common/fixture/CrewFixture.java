package com.retrip.crew.common.fixture;

import com.retrip.crew.application.in.request.crew.CrewCreateRequest;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMember;
import com.retrip.crew.domain.entity.CrewMemberRole;
import com.retrip.crew.domain.entity.CrewMembers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CrewFixture {
    public static final UUID TRIP_ID = UUID.fromString("b56d1d03-894a-4fe5-afbe-19be496bc1b4");
    public static final UUID LEADER_ID = UUID.fromString("caec62d1-f29d-477d-9743-292f48cc66bb");
    public static final UUID MEMBER_ID = UUID.fromString("85e05380-3693-4f3f-b74b-203715d15df8");
    public static final UUID 정수_ID = UUID.fromString("a7f7215b-081a-42f4-b3e2-f06393de2f8b");
    public static final UUID 홍석_ID = UUID.fromString("bf97d20b-d1f7-46a9-8362-11b9fa02d67d");
    public static final UUID 준호_ID = UUID.fromString("8b9b67fd-1d88-4b30-bfea-cd8f89fc10d9");
    public static final UUID 지수_ID = UUID.fromString("de3b60d2-5672-464d-8769-bf5c9de5eaff");
    public static final UUID 혁진_ID = UUID.fromString("42880aaf-4b97-4b0c-8a8a-72df4bb592f6");

    public static List<String> createDefaultQuestions() {
        return List.of(
                "크루에 지원한 이유는 무엇인가요?",
                "어떤 활동을 기대하고 있나요?",
                "자신을 한 문장으로 표현한다면?",
                "크루에서 어떤 역할을 하고 싶나요?",
                "추가로 하고 싶은 말이 있나요?"
        );
    }

    public static CrewCreateRequest createCrewRequest(UUID memberId, String title, String description, int maxMembers, List<String> questions) {
        return new CrewCreateRequest(
                memberId,
                title,
                description,
                maxMembers,
                questions
        );
    }

    public static List<CrewCreateRequest> createMultipleCrews(int count, UUID memberId, String baseTitle, String baseDescription, int maxMembers, List<String> questions) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    String title = baseTitle + " " + (i + 1);
                    String description = baseDescription + " " + (i + 1);
                    return createCrewRequest(memberId, title, description, maxMembers, questions);
                })
                .collect(Collectors.toList());
    }

    public static Crew createCrew(UUID leaderId) {
        return Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                leaderId,
                createDefaultQuestions());
    }

    public static Crew createCrewWithMutableMembers(UUID leaderId) {
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                leaderId,
                createDefaultQuestions());
        List<CrewMember> crewMemberList = new ArrayList<>(5);
        crewMemberList.add(new CrewMember(crew, leaderId, CrewMemberRole.LEADER));
        crewMemberList.add(new CrewMember(crew, 정수_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 홍석_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 준호_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 지수_ID, CrewMemberRole.PARTICIPANT));
        crewMemberList.add(new CrewMember(crew, 혁진_ID, CrewMemberRole.PARTICIPANT));
        CrewMembers crewMembers = new CrewMembers(crew, leaderId);
        ReflectionTestUtils.setField(crewMembers, "values", crewMemberList);
        ReflectionTestUtils.setField(crew, "crewMembers", crewMembers);
        return crew;
    }

    public static Crew createCrewWithMembers(UUID leaderId) {
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                leaderId,
                createDefaultQuestions());
        List<CrewMember> crewMemberList = List.of(
                new CrewMember(crew, leaderId, CrewMemberRole.LEADER),
                new CrewMember(crew, 정수_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 홍석_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 준호_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 지수_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 혁진_ID, CrewMemberRole.PARTICIPANT)
        );
        CrewMembers crewMembers = new CrewMembers(crew, leaderId);
        ReflectionTestUtils.setField(crewMembers, "values", crewMemberList);
        ReflectionTestUtils.setField(crew, "crewMembers", crewMembers);
        return crew;
    }

    public static Crew createCrewWithMembers(UUID leaderId, int maxMembers) {
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                maxMembers,
                leaderId,
                createDefaultQuestions());
        List<CrewMember> crewMemberList = List.of(
                new CrewMember(crew, leaderId, CrewMemberRole.LEADER),
                new CrewMember(crew, 정수_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 홍석_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 준호_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 지수_ID, CrewMemberRole.PARTICIPANT),
                new CrewMember(crew, 혁진_ID, CrewMemberRole.PARTICIPANT)
        );
        CrewMembers crewMembers = new CrewMembers(crew, leaderId);
        ReflectionTestUtils.setField(crewMembers, "values", crewMemberList);
        ReflectionTestUtils.setField(crew, "crewMembers", crewMembers);
        return crew;
    }
}
