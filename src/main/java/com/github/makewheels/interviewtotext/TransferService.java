package com.github.makewheels.interviewtotext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.github.makewheels.interviewtotext.util.HtmlUtil;
import com.github.makewheels.interviewtotext.util.qcloud.QcloudAsrUtil;
import com.github.makewheels.interviewtotext.util.qcloud.QcloudCosUtil;
import com.github.makewheels.interviewtotext.util.qcloud.QcloudMediaUtil;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.asr.v20190614.models.SentenceDetail;
import com.tencentcloudapi.asr.v20190614.models.TaskStatus;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class TransferService {

    public void handleObject(String videoKey) {
        new Thread(() -> {
            log.info("service开始处理 videoKey = " + videoKey);
            //视频转音频
            String baseName = FilenameUtils.getBaseName(videoKey);
            String outputPath = "audio/";
            String audioKey = outputPath + baseName + "_transcode_1088895.mp3";
            log.info("开始转音频: videoKey = " + videoKey + ", audioKey = " + audioKey);
            String mediaTaskId = QcloudMediaUtil.submit(videoKey, outputPath);
            for (int i = 0; i < 10 * 60; i++) {
                log.info("查询media第" + (i + 1) + "次 mediaTaskId = " + mediaTaskId + " baseName = " + baseName);
                if (QcloudMediaUtil.queryTask(mediaTaskId)) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("转音频完成 audioKey = " + audioKey);

            //音频识别为文字
            log.info("开始asr识别 audioKey = " + audioKey);
            long asrTaskId = QcloudAsrUtil.submit(QcloudCosUtil.getUrl(audioKey));
            DescribeTaskStatusResponse asrResponse = null;
            for (int i = 0; i < 10 * 60; i++) {
                asrResponse = QcloudAsrUtil.queryTask(asrTaskId);
                log.info("第" + (i + 1) + "次查询asr结果 " + JSON.toJSONString(asrResponse));
                if (asrResponse == null)
                    throw new NullPointerException();
                TaskStatus data = asrResponse.getData();
                if (data == null)
                    throw new NullPointerException();
                long status = data.getStatus();
                if (status == 2) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //保存识别结果到对象存储
            File asrJsonFile = new File(SystemUtils.getUserHome(), baseName + ".json");
            FileUtil.writeString(JSON.toJSONString(asrResponse), asrJsonFile, StandardCharsets.UTF_8);
            log.info("保存asr识别结果到对象存储 asrJsonFile = " + asrJsonFile.getPath());
            String asrKey = "asr/" + asrJsonFile.getName();
            QcloudCosUtil.upload(asrKey, asrJsonFile);
            log.info("删除asr json识别结果文件 " + asrJsonFile.getPath() + " 删除结果 " + asrJsonFile.delete());

            //写网页文件
            File htmlFile = new File(SystemUtils.getUserHome(), baseName + ".html");
            String result = asrResponse.getData().getResult();
            log.info("写网页文件 htmlFile = " + htmlFile.getPath());
            HtmlUtil.writeFile(result.split("\n"), htmlFile,videoKey,audioKey,asrKey);

            //网页文件上传对象存储
            log.info("保存网页文件到对象存储 htmlFile = " + htmlFile.getPath());
            QcloudCosUtil.upload("text/" + htmlFile.getName(), htmlFile);
            log.info("删除网页文件 " + htmlFile.getPath() + " 删除结果 " + htmlFile.delete());
        }).start();
    }

}
