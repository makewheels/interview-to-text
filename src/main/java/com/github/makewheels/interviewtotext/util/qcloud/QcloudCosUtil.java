package com.github.makewheels.interviewtotext.util.qcloud;

import cn.hutool.core.util.URLUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

public class QcloudCosUtil {
    private static final String region = "ap-beijing";
    private static final String bucketName = "offer-1253319037";

    public static String getRegion() {
        return region;
    }

    public static String getBucketName() {
        return bucketName;
    }

    private static final COSClient cosClient = new COSClient(
            new BasicCOSCredentials("AKIDZ2IK8h3B5pRpzvd7NRIH6jHez71h9QWQ",
                    "jEVcXGm5C6mGWZP0NWR9fy3uZ6RcaKq8"),
            new ClientConfig(new Region(region)));

    public static List<COSObjectSummary> getObjects(String path) {
        ObjectListing objectListing = cosClient.listObjects(bucketName, path);
        return objectListing.getObjectSummaries();
    }

    public static String getUrl(String key) {
        String url = "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
        return URLUtil.encode(url);
    }

    public static String upload(String key, File file) {
        cosClient.putObject(bucketName, key, file);
        return getUrl(key);
    }

}
