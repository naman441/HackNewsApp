package com.growindigo.aimt.mynewsapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AppUtils {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return formatter.format(date);
    }

    // getting local-date from unix timestamp
    public static String getTime(int time){
        LocalDateTime d = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
        return formatDate(d.toLocalDate());
    }

}
