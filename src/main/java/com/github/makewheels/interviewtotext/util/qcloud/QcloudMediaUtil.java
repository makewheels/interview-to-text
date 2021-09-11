package com.github.makewheels.interviewtotext.util.qcloud;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.mps.v20190612.MpsClient;
import com.tencentcloudapi.mps.v20190612.models.*;

public class QcloudMediaUtil {
    private static final Credential cred = new Credential("AKIDWwAAyYu1uZzB7oGTQAfuugDVXKsBG7j5",
            "A5lCOL3XllW2JL8j5D7lAKFvNaYGkz2t");

    public static String submit(String objectKey, String output) {
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
        cosInputInfo1.setBucket(QcloudCosUtil.getBucketName());
        cosInputInfo1.setRegion(QcloudCosUtil.getRegion());
        cosInputInfo1.setObject(objectKey);
        mediaInputInfo1.setCosInputInfo(cosInputInfo1);

        req.setInputInfo(mediaInputInfo1);

        TaskOutputStorage taskOutputStorage1 = new TaskOutputStorage();
        taskOutputStorage1.setType(output);
        CosOutputStorage cosOutputStorage1 = new CosOutputStorage();
        cosOutputStorage1.setBucket(QcloudCosUtil.getBucketName());
        cosOutputStorage1.setRegion(QcloudCosUtil.getRegion());
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
        ProcessMediaResponse resp = null;
        try {
            resp = client.ProcessMedia(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        if (resp == null) {
            return null;
        }
        return resp.getTaskId();
    }
}
