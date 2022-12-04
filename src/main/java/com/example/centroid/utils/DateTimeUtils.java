package com.example.centroid.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@Slf4j
public class DateTimeUtils {
    public static ZonedDateTime dateTimeInIST(){

        LocalDateTime localNow = LocalDateTime.now();
        ZonedDateTime zonedUTC = localNow.atZone(ZoneId.of("UTC"));
        ZonedDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return zonedIST;
    }
}
