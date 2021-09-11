package com.github.makewheels.interviewtotext.util.qcloud;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskResponse;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

public class QcloudAsrUtil {
    private static final Credential cred = new Credential("AKIDYtSnrpjI3YqoZ2sQZaRApTt2vXv3oqeQ",
            "jCPREl14tQiiEQLzMUMxJwKdnBLL4i7J");

    public static long submit(String url) {
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("asr.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        AsrClient client = new AsrClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        CreateRecTaskRequest req = new CreateRecTaskRequest();
        req.setEngineModelType("8k_zh");
        req.setChannelNum(1L);
        req.setResTextFormat(0L);
        req.setSourceType(0L);
        req.setSpeakerDiarization(1L);
        req.setSpeakerNumber(2L);
        req.setUrl(url);
        // 返回的resp是一个CreateRecTaskResponse的实例，与请求对象对应
        CreateRecTaskResponse resp = null;
        try {
            resp = client.CreateRecTask(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        if (resp == null) {
            return 0;
        }
        return resp.getData().getTaskId();
    }

    public static String[] queryTask(long taskId) {
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
        req.setTaskId(taskId);
        // 返回的resp是一个DescribeTaskStatusResponse的实例，与请求对象对应
        DescribeTaskStatusResponse resp = null;
        try {
            resp = client.DescribeTaskStatus(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        if (resp == null || resp.getData().getStatus() != 2) {
            return null;
        }
        String result = resp.getData().getResult();
        return result.split("\n");
    }
}
