package com.example.tingeso1.utils;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MiscUtils {

    //Calcula años de servicio de un empleado a partir de fecha de inicio de su trabajo actual
    public static int getYearsUntilNow(ZonedDateTime startDate){
        ZonedDateTime actual = ZonedDateTime.now();
        return (int) startDate.until(actual, ChronoUnit.YEARS);
    }
}