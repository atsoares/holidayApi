package com.bluestone.pim.test.holidayapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The POJO class HolidayExternalApiResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HolidayExternalApiResponse {

    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;

}
