package com.github.makewheels.interviewtotext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("qcloud/cos")
@Slf4j
public class QcloudCosController {
    @Resource
    private TransferService transferService;

    @RequestMapping("callback")
    public String callback(@RequestParam String objectname) {
        log.info("收到腾讯云对象存储上传回调，key = " + objectname);
        if (!objectname.startsWith("offer")) {
            System.out.println("object被忽略");
        }
        transferService.handleObject(objectname);
        return "spring boot response " + objectname;
    }
}
