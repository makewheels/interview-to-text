package com.github.makewheels.interviewtotext;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.region.Region;

import java.util.Collections;
import java.util.List;

public class QcloudCosUtil {
    private static final String secretId = "AKIDxiEUUEhurXxA8pGRLzLQEm33nGSN2RZQ";
    private static final String secretKey = "b3FAoCKBQUIq9JEtIRLzNUN2RbXDdnhL";

    private static final COSClient cosClient = new COSClient(new BasicCOSCredentials(secretId, secretKey),
            new ClientConfig(new Region("ap-beijing")));

    public static void getObjects(String path) {
        ObjectListing offer = cosClient.listObjects("bucket-1253319037", path);
        List<COSObjectSummary> objectSummaries = offer.getObjectSummaries();
        for (COSObjectSummary objectSummary : objectSummaries) {
            System.out.println(objectSummary.getKey() + " " + objectSummary.getSize()
                    + " " + objectSummary.getLastModified());
        }
    }

    public static void main(String[] args) {
        getObjects("offer");
    }
}
