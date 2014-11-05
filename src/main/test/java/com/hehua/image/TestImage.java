package com.hehua.image;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.*;
import com.google.common.collect.Iterables;
import com.hehua.framework.image.ImageUtils;
import junit.framework.TestCase;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by hesheng on 14-9-24.
 */
public class TestImage extends TestCase {
//    private OSSClient shenzhenOssClient = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com",
//            "NXOAI03gz4VVRhZw", "tp51Wh42lvaZNy0xIYhyKhsE0XXwkq");
    OSSClient shenzhenOssClient = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com",
            "NXOAI03gz4VVRhZw", "tp51Wh42lvaZNy0xIYhyKhsE0XXwkq");

    OSSClient beijinOssClient = new OSSClient("http://oss-cn-beijing.aliyuncs.com",
            "NXOAI03gz4VVRhZw", "tp51Wh42lvaZNy0xIYhyKhsE0XXwkq");
    @Test
    public void testStoreImage() throws IOException {
    //icon_90day_return_good.png //icon_real_quantity
        byte[] imageData = FileUtils.readFileToByteArray(new File(
                "/Users/hesheng/workspace/tmp/list_icon_exemption_from_postage.png"));
        MagickImage magickImage = null;
        int width = 0;
        int height = 0;
        String format = null;
        try {
            magickImage = new MagickImage(new ImageInfo(), imageData);
            Dimension dimension = magickImage.getDimension();
            format = magickImage.getImageFormat().toLowerCase();
            width = (int) dimension.getWidth();
            height = (int) dimension.getHeight();
        } catch (MagickException e1) {
            throw new RuntimeException("error", e1);
        } finally {
            if (magickImage != null) {
                magickImage.destroyImages();
            }
        }

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imageData.length);
        meta.setContentType(ImageUtils.getMimeType(format));
        String filename = "icon_free_postage" + "." + format;

        try (InputStream inputStream = new ByteArrayInputStream(imageData)) {
            PutObjectResult putObject = shenzhenOssClient.putObject("hbimg", filename,
                    inputStream, meta);
            System.out.println(putObject + " filename=" + filename );
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void testUID() {
       System.out.println(UUID.randomUUID().toString());
    }

    public void testShenzhenImage() {
        String marker = "";
        int totalSize = 0;
        for (int i = 0; i <= 500; i++) {
            ListObjectsRequest request = new ListObjectsRequest("hbimg", "", marker, "", 200);
            ObjectListing listObjects = shenzhenOssClient.listObjects(request);
            java.util.List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            totalSize += objectSummaries.size();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();

                System.out.println(key);
            }

            if (objectSummaries.size() < 100) {
                System.out.println("page=" +i);
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }
        System.out.println(totalSize);
    }

    public void testBeijingImage() {
        Set<String> set = new HashSet<>(2000);
        String marker = "";
        for (int i = 0; i <= 500; i++) {
            ListObjectsRequest request = new ListObjectsRequest("hhimg", "", marker, "", 200);
            ObjectListing listObjects = beijinOssClient.listObjects(request);
            java.util.List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();
                set.add(key);
            }

            if (objectSummaries.size() < 100) {
                System.out.println("page=" +i);
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }
        System.out.println(set.size());
    }

    private String shenzhenBuket = "hbimg";

    private String beijingBucket = "hhimg";

    @Test
    public void testSynchronizedFromShenzhenToBeijingDiffSize() {
        Set<String> beijingSet = new HashSet<>(4000);
        String marker = "";
        for (int i = 0; i <= 500; i++) {
            ListObjectsRequest request = new ListObjectsRequest(beijingBucket, "", marker, "", 200);
            ObjectListing listObjects = beijinOssClient.listObjects(request);
            java.util.List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();
                beijingSet.add(key.trim());
            }

            if (objectSummaries.size() < 100) {
                System.out.println("beijing page=" +i);
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }

        marker = "";
        int copyTotal = 0;
        for (int i = 0; i <= 500; i++) {
            ListObjectsRequest request = new ListObjectsRequest(shenzhenBuket, "", marker, "", 200);
            ObjectListing listObjects = shenzhenOssClient.listObjects(request);
            java.util.List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();
                key = key.trim();
                if (!beijingSet.contains(key)) {
                    System.out.println("shenzhen key copy beijing,key=" + key);
                    beijingSet.add(key);
                    copyTotal++;
//                    OSSObject ossobject = shenzhenOssClient.getObject(shenzhenBuket, key);
//                    InputStream input = ossobject.getObjectContent();
//                    ObjectMetadata meta = new ObjectMetadata();
//                    meta.setContentLength(ossobject.getObjectMetadata().getContentLength());
//                    meta.setContentType(ossobject.getObjectMetadata().getContentType());
//
//                    beijinOssClient.putObject(beijingBucket, key, input, meta);
                }

            }

            if (objectSummaries.size() < 100) {
                System.out.println("shenzhen page=" +i);
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }

        System.out.println("copy total size=" + copyTotal);
        System.out.println("set=" + beijingSet);

    }


    @Test
    public void testDataSynchronizedFromShenzhenToBeijing() {
        Set<String> beijingSet = new HashSet<>(4000);
        String marker = "";
        for (int i = 0; i <= 500; i++) {
            ListObjectsRequest request = new ListObjectsRequest(beijingBucket, "", marker, "", 200);
            ObjectListing listObjects = beijinOssClient.listObjects(request);
            java.util.List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();
                beijingSet.add(key.trim());
            }

            if (objectSummaries.size() < 100) {
                System.out.println("beijing page=" +i);
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }

        marker = "";
        int copyTotal = 0;
        for (int i = 0; i <= 500; i++) {
            ListObjectsRequest request = new ListObjectsRequest(shenzhenBuket, "", marker, "", 200);
            System.out.println("execute shenzhen page=" + i);
            ObjectListing listObjects = shenzhenOssClient.listObjects(request);
            java.util.List<OSSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
            for (OSSObjectSummary object : objectSummaries) {
                String key = object.getKey();
                key = key.trim();
                if (!beijingSet.contains(key)) {
                    System.out.println("shenzhen key copy beijing,key=" + key);
                    beijingSet.add(key);
                    copyTotal++;
                    OSSObject ossobject = shenzhenOssClient.getObject(shenzhenBuket, key);
                    InputStream input = ossobject.getObjectContent();
                    ObjectMetadata meta = new ObjectMetadata();
                    meta.setContentLength(ossobject.getObjectMetadata().getContentLength());
                    meta.setContentType(ossobject.getObjectMetadata().getContentType());

                    beijinOssClient.putObject(beijingBucket, key, input, meta);
                }

            }

            if (objectSummaries.size() < 100) {
                System.out.println("shenzhen page=" +i);
                break;
            }
            marker = Iterables.getLast(objectSummaries).getKey();
        }

        System.out.println("copy total size=" + copyTotal);
        System.out.println("set=" + beijingSet);

    }

}
