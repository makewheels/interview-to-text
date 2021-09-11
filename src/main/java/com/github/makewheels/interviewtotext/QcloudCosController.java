package com.github.makewheels.interviewtotext;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("qcloud/cos")
public class QcloudCosController {
    @RequestMapping("callback")
    public String callback(@RequestParam String objectname) {
        System.out.println(System.currentTimeMillis());
        System.out.println(objectname);
        return "spring boot response " + objectname;
    }
}
