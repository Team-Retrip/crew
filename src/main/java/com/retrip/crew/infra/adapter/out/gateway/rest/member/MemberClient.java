package com.retrip.crew.infra.adapter.out.gateway.rest.member;

import com.retrip.crew.infra.adapter.out.gateway.rest.member.response.MemberResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

//@HttpExchange(url = "${apis.urls.member}")
public interface MemberClient {
    //@GetExchange("/{memberId}")
    MemberResponse getMember(@PathVariable("memberId") UUID memberId);
}
