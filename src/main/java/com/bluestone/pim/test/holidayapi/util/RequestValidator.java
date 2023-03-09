package com.bluestone.pim.test.holidayapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Set;

public final class RequestValidator {
    private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

    private RequestValidator() {}

    public static boolean isValidDate(String date){
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidISOCountry(String s) {
        return ISO_COUNTRIES.contains(s);
    }
}