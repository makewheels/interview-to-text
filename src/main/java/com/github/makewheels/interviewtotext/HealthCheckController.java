package com.github.makewheels.interviewtotext;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @RequestMapping("healthCheck")
    public String healthCheck() {
        return "ok";
    }
}
