package com.retrip.crew.domain.converter;

import com.retrip.crew.domain.vo.DemandStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DemandStatusConverter implements AttributeConverter<DemandStatus, String> {
    @Override
    public String convertToDatabaseColumn(DemandStatus status) {
        if(status == null){
            throw new NullPointerException("crewMemberRole을 DB 칼럼으로 변경하는 과정에서 null이 포함되었습니다.");
        }
        return status.getCode();
    }

    @Override
    public DemandStatus convertToEntityAttribute(String dbData) {
        if(dbData == null){
            throw new NullPointerException("CrewMember 테이블의 role 값이 null입니다.");
        }
        return DemandStatus.codeOf(dbData);
    }
}
