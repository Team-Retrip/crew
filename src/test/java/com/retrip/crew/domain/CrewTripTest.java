package com.retrip.crew.domain;

import com.retrip.crew.common.fixture.CrewFixture;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CrewTripTest {

    @ParameterizedTest
    @CsvSource({"EXCLUSION,true", "INCLUSION,false"})
    void 크루_여행_타입이_크루원_전용이면_ture_크루원_포함이면_false를_반환한다(CrewTrip.CrewTripType type, boolean expected) {
        CrewTrip crewTrip = new CrewTrip(CrewFixture.TRIP_ID, type);
        boolean actual = crewTrip.isImpossibleWithdrawal();
        assertThat(actual).isEqualTo(expected);
    }
}
