package com.github.makewheels.interviewtotext.util.qcloud;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.mps.v20190612.MpsClient;
import com.tencentcloudapi.mps.v20190612.models.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        CosInputInfo cosInputInfo = new CosInputInfo();
        cosInputInfo.setBucket(QcloudCosUtil.getBucketName());
        cosInputInfo.setRegion(QcloudCosUtil.getRegion());
        cosInputInfo.setObject(objectKey);
        mediaInputInfo1.setCosInputInfo(cosInputInfo);

        req.setInputInfo(mediaInputInfo1);

        TaskOutputStorage taskOutputStorage = new TaskOutputStorage();
        taskOutputStorage.setType("COS");
        CosOutputStorage cosOutputStorage = new CosOutputStorage();
        cosOutputStorage.setBucket(QcloudCosUtil.getBucketName());
        cosOutputStorage.setRegion(QcloudCosUtil.getRegion());
        taskOutputStorage.setCosOutputStorage(cosOutputStorage);

        req.setOutputStorage(taskOutputStorage);

        req.setOutputDir(output);
        MediaProcessTaskInput mediaProcessTaskInput = new MediaProcessTaskInput();

        TranscodeTaskInput[] transcodeTaskInputs = new TranscodeTaskInput[1];
        TranscodeTaskInput transcodeTaskInput = new TranscodeTaskInput();
        transcodeTaskInput.setDefinition(1088895L);
        transcodeTaskInputs[0] = transcodeTaskInput;

        mediaProcessTaskInput.setTranscodeTaskSet(transcodeTaskInputs);

        req.setMediaProcessTask(mediaProcessTaskInput);

        // 返回的resp是一个ProcessMediaResponse的实例，与请求对象对应
        ProcessMediaResponse resp = null;
        try {
            resp = client.ProcessMedia(req);
            log.info("media处理, objectKey = " + objectKey + " output = " + output + " 腾讯返回：");
            log.info(JSON.toJSONString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        if (resp == null) {
            return null;
        }
        return resp.getTaskId();
    }

    public static boolean queryTask(String taskId) {
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("mps.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        MpsClient client = new MpsClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeTaskDetailRequest req = new DescribeTaskDetailRequest();
        req.setTaskId(taskId);
        // 返回的resp是一个DescribeTaskDetailResponse的实例，与请求对象对应
        DescribeTaskDetailResponse resp = null;
        try {
            resp = client.DescribeTaskDetail(req);
            log.info("media查询, taskId = " + taskId + " 腾讯返回：" + JSON.toJSONString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        if (resp == null) {
            return false;
        }
        return resp.getStatus().equals("FINISH");
    }
}
