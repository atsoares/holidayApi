package com.bluestone.pim.test.holidayapi;

import com.bluestone.pim.test.holidayapi.controller.HolidayController;
import com.bluestone.pim.test.holidayapi.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {

    private static final String HOLIDAY_ENDPOINT = "/api/holiday";

    @InjectMocks
    private HolidayController holidayController;

    @Mock
    private HolidayService holidayService;
    private MockMvc mockMvc;

    private static String date;
    private static String countryCode1;
    private static String countryCode2;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
    }

    @Test
    void givenValidRequestParams_whenGetNextHolidaySameDay_thenReturnOk() throws Exception {

        givenAValidDate();
        givenAValidCountryCode1();
        givenAValidCountryCode2();

        whenCallGetNextHolidaySameDay();

        thenExpectOkStatus();
        thenExpectHolidayServiceGetNextHolidaySameDay();
    }

    // Givens

    private void givenAValidDate(){
        date = "2022-03-04";
    }
    private void givenAValidCountryCode1(){
        countryCode1 = "BR";
    }
    private void givenAValidCountryCode2(){
        countryCode2 = "PL";
    }

    // When

    private void whenCallGetNextHolidaySameDay() throws Exception {
        response = mockMvc.perform(
                get(HOLIDAY_ENDPOINT)
                        .param("date", date)
                        .param("countryCode1", countryCode1)
                        .param("countryCode2", countryCode2))
                .andReturn().getResponse();
    }

    // Thens

    private void thenExpectOkStatus() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private void thenExpectHolidayServiceGetNextHolidaySameDay() {
        verify(holidayService, times(1)).getNextHolidaySameDay(date,countryCode1,countryCode2);
    }
}
