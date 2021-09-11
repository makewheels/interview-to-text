package com.github.makewheels.interviewtotext;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.mps.v20190612.MpsClient;
import com.tencentcloudapi.mps.v20190612.models.*;

public class QcloudMediaUtil {
    public static void main(String[] args) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往 https://console.cloud.tencent.com/cam/capi 网站进行获取
            Credential cred = new Credential("AKIDWwAAyYu1uZzB7oGTQAfuugDVXKsBG7j5",
                    "A5lCOL3XllW2JL8j5D7lAKFvNaYGkz2t");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("mps.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            MpsClient client = new MpsClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ProcessMediaRequest req = new ProcessMediaRequest();
            MediaInputInfo mediaInputInfo1 = new MediaInputInfo();
            mediaInputInfo1.setType("COS");
            CosInputInfo cosInputInfo1 = new CosInputInfo();
            cosInputInfo1.setBucket("bucket-1253319037");
            cosInputInfo1.setRegion("ap-beijing");
            cosInputInfo1.setObject("offer/2021.09.09 美团.mp4");
            mediaInputInfo1.setCosInputInfo(cosInputInfo1);

            req.setInputInfo(mediaInputInfo1);

            TaskOutputStorage taskOutputStorage1 = new TaskOutputStorage();
            taskOutputStorage1.setType("COS");
            CosOutputStorage cosOutputStorage1 = new CosOutputStorage();
            cosOutputStorage1.setBucket("bucket-1253319037");
            cosOutputStorage1.setRegion("ap-beijing");
            taskOutputStorage1.setCosOutputStorage(cosOutputStorage1);

            req.setOutputStorage(taskOutputStorage1);

            req.setOutputDir("movie/");
            MediaProcessTaskInput mediaProcessTaskInput1 = new MediaProcessTaskInput();

            TranscodeTaskInput[] transcodeTaskInputs1 = new TranscodeTaskInput[1];
            TranscodeTaskInput transcodeTaskInput1 = new TranscodeTaskInput();
            transcodeTaskInput1.setDefinition(1088409L);
            transcodeTaskInputs1[0] = transcodeTaskInput1;

            mediaProcessTaskInput1.setTranscodeTaskSet(transcodeTaskInputs1);

            req.setMediaProcessTask(mediaProcessTaskInput1);

            // 返回的resp是一个ProcessMediaResponse的实例，与请求对象对应
            ProcessMediaResponse resp = client.ProcessMedia(req);
            // 输出json格式的字符串回包
            System.out.println(ProcessMediaResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
