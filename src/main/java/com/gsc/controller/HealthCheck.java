package com.gsc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthCheck {

    @RequestMapping(path = "/healthcheck", method = RequestMethod.GET)
    @ResponseBody public String healthcheck() {
        return "heathcheck, it works";
    }
}
