package com.mbv.api.util;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Date;

/**
 * Created by arindamnath on 11/03/16.
 */
public class DateUtil {

    public static int getAge(Date date) {
        LocalDate birthdate = new LocalDate(date);
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdate, now);
        return age.getYears();
    }
}
