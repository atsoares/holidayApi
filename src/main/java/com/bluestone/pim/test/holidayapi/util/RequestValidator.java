package com.bluestone.pim.test.holidayapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Set;

/**
 * The class RequestValidator
 *
 */
public final class RequestValidator {

    /**
     * Constant set of ISOCountries
     */
    private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

    /**
     * Default private constructor RequestValidator
     */
    private RequestValidator() {}

    /**
     * Method isValidDate.
     *
     * @param date String date.
     * @return Return boolean
     */
    public static boolean isValidDate(String date){
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Method isValidISOCountry.
     *
     * @param countryCode String country code.
     * @return Return boolean
     */
    public static boolean isValidISOCountry(String countryCode) {
        return ISO_COUNTRIES.contains(countryCode);
    }
}