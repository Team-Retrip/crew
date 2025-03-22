package com.retrip.crew.domain.converter;

import com.retrip.crew.domain.vo.RecruitmentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RecruitmentStatusConverter implements AttributeConverter<RecruitmentStatus, String> {
    @Override
    public String convertToDatabaseColumn(RecruitmentStatus status) {
        if(status == null){
            throw new NullPointerException("crewMemberRole을 DB 칼럼으로 변경하는 과정에서 null이 포함되었습니다.");
        }
        return status.getCode();
    }

    @Override
    public RecruitmentStatus convertToEntityAttribute(String dbData) {
        if(dbData == null){
            throw new NullPointerException("CrewMember 테이블의 role 값이 null입니다.");
        }
        return RecruitmentStatus.codeOf(dbData);
    }
}
