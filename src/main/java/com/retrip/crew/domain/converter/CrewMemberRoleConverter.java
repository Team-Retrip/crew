package com.retrip.crew.domain.converter;

import com.retrip.crew.domain.entity.CrewMemberRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CrewMemberRoleConverter implements AttributeConverter<CrewMemberRole, String> {
    @Override
    public String convertToDatabaseColumn(CrewMemberRole crewMemberRole) {
        if(crewMemberRole == null){
            throw new NullPointerException("crewMemberRole을 DB 칼럼으로 변경하는 과정에서 null이 포함되었습니다.");
        }
        return crewMemberRole.getCode();
    }

    @Override
    public CrewMemberRole convertToEntityAttribute(String dbData) {
        if(dbData == null){
            throw new NullPointerException("CrewMember 테이블의 role 값이 null입니다.");
        }
        return CrewMemberRole.codeOf(dbData);
    }
}
