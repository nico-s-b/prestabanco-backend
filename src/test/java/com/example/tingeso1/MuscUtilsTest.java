package com.example.tingeso1;

import com.example.tingeso1.utils.MiscUtils;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MuscUtilsTest {

    @Test
    public void whenStartDateOneYear_ThenYearsUntilDate1(){
        //Given
        ZonedDateTime oneYearAgo = ZonedDateTime.now().minusYears(1);

        //When
        int years = MiscUtils.getYearsUntilNow(oneYearAgo);

        //Then
        assertThat(years).isEqualTo(1);
    }

    @Test
    public void whenStartDateLessThanOneYear_ThenYearsUntilDate0(){
        //Given
        ZonedDateTime oneYearAgo = ZonedDateTime.now().minusYears(1).plusDays(1);

        //When
        int years = MiscUtils.getYearsUntilNow(oneYearAgo);

        //Then
        assertThat(years).isEqualTo(0);
    }
}
