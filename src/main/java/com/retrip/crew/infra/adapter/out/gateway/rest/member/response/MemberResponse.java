package com.retrip.crew.infra.adapter.out.gateway.rest.member.response;



import java.util.UUID;

public record MemberResponse (
        UUID id,
        String name,
        int age,
        GenderResponse gender
){
    public enum GenderResponse{
        MALE, FEMALE
    }
}
