package com.bluestone.pim.test.holidayapi.service;

import com.bluestone.pim.test.holidayapi.exception.ResponseException;
import com.bluestone.pim.test.holidayapi.model.HolidayResponse;

/**
 * The interface HolidayService
 */
public interface HolidayService {

    /**
     * Method getNextHolidaySameDay to be implemented.
     *
     * @param date String date to search.
     * @param code1 String country code1.
     * @param code2 String country code2.
     * @return Return HolidayResponse object
     */
    HolidayResponse getNextHolidaySameDay(String date, String code1, String code2) throws ResponseException;

}
