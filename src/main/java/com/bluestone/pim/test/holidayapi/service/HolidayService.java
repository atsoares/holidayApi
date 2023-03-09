package com.bluestone.pim.test.holidayapi.service;

import com.bluestone.pim.test.holidayapi.exception.ResponseException;
import com.bluestone.pim.test.holidayapi.model.HolidayResponse;

public interface HolidayService {

    HolidayResponse getNextHolidaySameDay(String date, String code1, String code2) throws ResponseException;

}
