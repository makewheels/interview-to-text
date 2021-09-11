package com.github.makewheels.interviewtotext.util.qcloud;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.util.List;

public class QcloudCosUtil {
    private static final String secretId = "AKIDZ2IK8h3B5pRpzvd7NRIH6jHez71h9QWQ";
    private static final String secretKey = "jEVcXGm5C6mGWZP0NWR9fy3uZ6RcaKq8";
    private static final String region = "ap-beijing";
    private static final String bucketName = "offer-1253319037";

    public static String getRegion() {
        return region;
    }

    public static String getBucketName() {
        return bucketName;
    }

    private static final COSClient cosClient = new COSClient(
            new BasicCOSCredentials(secretId, secretKey),
            new ClientConfig(new Region(region)));

    public static List<COSObjectSummary> getObjects(String path) {
        ObjectListing objectListing = cosClient.listObjects(bucketName, path);
        return objectListing.getObjectSummaries();
    }

    public String getUrl(String key) {
        return "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
    }

    public String upload(String key, File file) {
        cosClient.putObject(bucketName, key, file);
        return getUrl(key);
    }

}
