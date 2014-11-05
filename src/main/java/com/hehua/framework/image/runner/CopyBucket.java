/**
 * 
 */
package com.hehua.framework.image.runner;

import java.io.InputStream;
import java.util.List;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.google.common.collect.Iterables;

/**
 * @author zhihua
 *
 */
public class CopyBucket {

    /**
     * @param args
     */
    public static void main(String[] args) {
        OSSClient beijinOssClient = new OSSClient("http://oss-cn-beijing.aliyuncs.com",
                "NXOAI03gz4VVRhZw", "tp51Wh42lvaZNy0xIYhyKhsE0XXwkq");

        OSSClient shenzhenOssClient = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com",
                "NXOAI03gz4VVRhZw", "tp51Wh42lvaZNy0xIYhyKhsE0XXwkq");

        String marker = "";
        for (int i = 0; i <= 100; i++) {
            ListObjectsRequest request = new ListObjectsRequest("hhimg", "", marker, "", 100);
            ObjectListing listObjects = beijinOssClient.listObjects(request);
            List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();
                System.out.println(key);

                OSSObject ossobject = beijinOssClient.getObject("hhimg", key);
                InputStream input = ossobject.getObjectContent();
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentLength(ossobject.getObjectMetadata().getContentLength());
                meta.setContentType(ossobject.getObjectMetadata().getContentType());

                shenzhenOssClient.putObject("hbimg", key, input, meta);

            }

            if (objectSummaries.size() < 100) {
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }
    }

}
