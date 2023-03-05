package com.bluestone.pim.test.holidayapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webclient")
@AllArgsConstructor
public class HolidayController {

    @RequestMapping("/test")
    public String test(){
        return "Test";
    }

}
