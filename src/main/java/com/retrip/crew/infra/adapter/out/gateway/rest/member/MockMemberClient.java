package com.retrip.crew.infra.adapter.out.gateway.rest.member;

import com.retrip.crew.infra.adapter.out.gateway.rest.member.response.MemberResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockMemberClient implements MemberClient {

    @Override
    public MemberResponse getMember(UUID memberId) {
        return new MemberResponse(
                UUID.randomUUID(),
                "김준호",
                30,
                MemberResponse.GenderResponse.MALE
        );
    }
}
