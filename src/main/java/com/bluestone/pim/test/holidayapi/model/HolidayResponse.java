package com.bluestone.pim.test.holidayapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The POJO class HolidayResponse
 */
@Data
@NoArgsConstructor
public class HolidayResponse {
    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("name1")
    private String name1;

    @JsonProperty("name2")
    private String name2;
}
