package com.github.makewheels.interviewtotext;

import com.github.makewheels.interviewtotext.util.HtmlUtil;
import com.github.makewheels.interviewtotext.util.qcloud.QcloudAsrUtil;
import com.github.makewheels.interviewtotext.util.qcloud.QcloudCosUtil;
import com.github.makewheels.interviewtotext.util.qcloud.QcloudMediaUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TransferService {

    public void handleObject(String objectname) {
        new Thread(() -> {
            //视频转音频
            String baseName = FilenameUtils.getBaseName(objectname);
            String audioFileKey = "audio/" + baseName + ".mp3";
            String mediaTaskId = QcloudMediaUtil.submit(objectname, audioFileKey);
            for (int i = 0; i < 10 * 60; i++) {
                if (QcloudMediaUtil.queryTask(mediaTaskId)) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //音频识别为文字
            long asrTaskId = QcloudAsrUtil.submit(QcloudCosUtil.getUrl(audioFileKey));
            String[] lines = null;
            for (int i = 0; i < 10 * 60; i++) {
                lines = QcloudAsrUtil.queryTask(asrTaskId);
                if (lines != null) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //写网页文件
            File file = new File(baseName + ".html");
            if (lines == null)
                throw new NullPointerException();
            HtmlUtil.writeFile(lines, file);

            //网页文件上传对象存储
            QcloudCosUtil.upload("text/" + file.getName(), file);
        }).start();
    }

}
