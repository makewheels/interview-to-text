package com.github.makewheels.interviewtotext;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

public class QcloudAsrUtil {
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
            CreateRecTaskRequest req = new CreateRecTaskRequest();
            req.setEngineModelType("8k_zh");
            req.setChannelNum(1L);
            req.setResTextFormat(0L);
            req.setSourceType(0L);
            req.setSpeakerDiarization(1L);
            req.setSpeakerNumber(2L);
            req.setUrl("https://bucket-1253319037.cos.ap-beijing.myqcloud.com/movie/2021.09.09%20%E7%BE%8E%E5%9B%A2_transcode_1010.mp3");
            // 返回的resp是一个CreateRecTaskResponse的实例，与请求对象对应
            CreateRecTaskResponse resp = client.CreateRecTask(req);
            // 输出json格式的字符串回包
            System.out.println(CreateRecTaskResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
