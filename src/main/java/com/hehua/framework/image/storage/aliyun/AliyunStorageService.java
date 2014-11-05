/**
 * 
 */
package com.hehua.framework.image.storage.aliyun;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.hehua.framework.image.FileMisUtils;
import com.hehua.framework.image.ImageUtils;
import com.hehua.framework.image.domain.Image;
import com.hehua.framework.image.storage.StorageService;
import org.apache.commons.io.FileUtils;

/**
 * @author zhihua
 *
 */
public class AliyunStorageService implements StorageService {
    public final static String BUCKET = "hhimg";

    public final static String VISTOR_URL = "http://img.hehuababy.com/";

    private  OSSClient ossClient = new OSSClient("http://oss-cn-beijing.aliyuncs.com",
            "NXOAI03gz4VVRhZw", "tp51Wh42lvaZNy0xIYhyKhsE0XXwkq");

    @Override
    public void store(Image image, byte[] imageBytes) throws IOException {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imageBytes.length);
        meta.setContentType(ImageUtils.getMimeType(image.getFormat()));
        String filename = image.getFid() + "." + image.getFormat();

        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            PutObjectResult putObject = ossClient.putObject(image.getBucket(), filename,
                    inputStream, meta);
        } catch (IOException e) {
            throw e;
        }
    }

    public void storeFile(String fileName, byte[] fileBytes) throws IOException {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(fileBytes.length);
        meta.setContentType(FileMisUtils.getMimeType(fileName));
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        PutObjectResult putObject = ossClient.putObject(BUCKET, fileName,
                inputStream, meta);
    }

}
