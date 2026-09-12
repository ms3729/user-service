package com.rata.userService.config;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import org.apache.commons.lang.RandomStringUtils;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    private static final SimpleDateFormat formatterWithTime = new SimpleDateFormat("yyyy/MM/dd HH:mm");


    private static final ULocale PERSIAN_LOCALE = new ULocale("fa@calendar=persian");
    private static final ULocale GREGORIAN_LOCALE = new ULocale("en@calendar=gregorian");
    private static final ZoneId IRAN_ZONE_ID = ZoneId.of("Asia/Tehran");

    public static String convertGregorianToJalali(Date gregorianDate) {
        Calendar gregorianCalendar = Calendar.getInstance(GREGORIAN_LOCALE);
        gregorianCalendar.clear();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone(IRAN_ZONE_ID.getId()));
        gregorianCalendar.setTime(gregorianDate);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd", PERSIAN_LOCALE);
        return df1.format(gregorianCalendar.getTime());
    }

    public static String convertGregorianToJalaliWithTime(Date gregorianDate) {
        Calendar gregorianCalendar = Calendar.getInstance(GREGORIAN_LOCALE);
        gregorianCalendar.clear();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone(IRAN_ZONE_ID.getId()));
        gregorianCalendar.setTime(gregorianDate);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd HH:mm", PERSIAN_LOCALE);
        return df1.format(gregorianCalendar.getTime());
    }

    public static Date convertStringDateToDateObject(String str) {
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertDateToDateStringWithTime(Date date) {
        return formatterWithTime.format(date);
    }

    public static String convertDateToDateString(Date date) {
        return formatter.format(date);
    }

    public static String generateSecurePassword() {

        // generate a string of upper case letters having length 2
        String upperCaseStr = RandomStringUtils.random(2, 65, 90, true, true);

        // generate a string of lower case letters having length 2
        String lowerCaseStr = RandomStringUtils.random(2, 97, 122, true, true);

        // generate a string of numeric letters having length 2
        String numbersStr = RandomStringUtils.randomNumeric(2);

        // generate a string of alphanumeric letters having length 2
        String totalCharsStr = RandomStringUtils.randomAlphanumeric(2);

        // concatenate all the strings into a single one
        String demoPassword = upperCaseStr.concat(lowerCaseStr)
                .concat(numbersStr)
                .concat(totalCharsStr);

        // create a list of Char that stores all the characters, numbers and special characters
        List<Character> listOfChar = demoPassword.chars()
                .mapToObj(data -> (char) data)
                .collect(Collectors.toList());

        // use shuffle() method of the Collections to shuffle the list elements
        Collections.shuffle(listOfChar);

        //generate a random string(secure password) by using list stream() method and collect() method
        return listOfChar.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static int generateRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(1000000);
    }

    public static String generateRandomCode() {
        Random rand = new Random();
        StringBuilder code = new StringBuilder(String.valueOf(rand.nextInt(1000000)));
        if (code.length() < 6) {
            int len = 6 - code.length();
            code.append("0".repeat(Math.max(0, len)));
        }
        return code.toString();
    }
}
