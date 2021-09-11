package com.github.makewheels.interviewtotext;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ZeroCopyHttpOutputMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QclousAsrQuery {
    public static void main(String[] args) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential("AKIDYtSnrpjI3YqoZ2sQZaRApTt2vXv3oqeQ",
                    "jCPREl14tQiiEQLzMUMxJwKdnBLL4i7J");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            AsrClient client = new AsrClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeTaskStatusRequest req = new DescribeTaskStatusRequest();
            req.setTaskId(1432886323L);
            // 返回的resp是一个DescribeTaskStatusResponse的实例，与请求对象对应
            DescribeTaskStatusResponse resp = client.DescribeTaskStatus(req);
            // 输出json格式的字符串回包
            String result = resp.getData().getResult();
            String[] split = result.split("\n");
            writeMarkdown(split);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }

    private static String addZero(String str) {
        String[] split = str.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        StringBuilder stringBuilder = new StringBuilder(5);
        if (hour <= 9) {
            stringBuilder.append("0");
        }
        stringBuilder.append(hour);
        stringBuilder.append(":");
        if (minute <= 9) {
            stringBuilder.append("0");
        }
        stringBuilder.append(minute);
        return stringBuilder.toString();
    }

    private static void writeMarkdown(String[] line) {
        List<Sentence> sentenceList = new ArrayList<>(line.length);
        for (String str : line) {
            String time = StringUtils.substringBetween(str, "[", "]");
            String content = str.substring(time.length() + 2).trim();
            if (content.endsWith("。")) {
                content = content.substring(0, content.length() - 1);
            }
            String[] timeSplit = time.split(",");
            String start = timeSplit[0].split("\\.")[0];
            String end = timeSplit[1].split("\\.")[0];
            int person = Integer.parseInt(timeSplit[2]);

            start = addZero(start);
            end = addZero(end);

            Sentence sentence = new Sentence();
            sentence.setStartTimeString(start);
            sentence.setEndTimeString(end);
            sentence.setPerson(person);
            sentence.setContent(content);
            sentenceList.add(sentence);
        }

        File file = new File("D:\\workSpace\\intellijidea\\interview-to-text\\src\\main\\resources",
                System.currentTimeMillis() + ".md");
        List<String> mdLines = new ArrayList<>(sentenceList.size());
        for (Sentence sentence : sentenceList) {
            String mdLine;
            if (sentence.getPerson() == 0) {
                mdLine = "<font color=\"green\">**[" + sentence.getStartTimeString() + "]** "
                        + sentence.getContent() + "</font>";
            } else {
//                mdLine = "<div align=\"right\"><font color= \"blue\">[**" + sentence.getStartTimeString() + "**] "
//                        + sentence.getContent() + "</font></div>";
                mdLine = "<font color= \"blue\">**[" + sentence.getStartTimeString() + "]** "
                        + sentence.getContent() + "</font>";
            }
            mdLines.add(mdLine);
        }
        for (String mdLine : mdLines) {
            System.out.println(mdLine);
        }
        try {
            FileUtils.writeLines(file, mdLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}