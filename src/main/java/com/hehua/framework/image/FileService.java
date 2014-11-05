package com.hehua.framework.image;

import com.hehua.framework.image.storage.aliyun.AliyunStorageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Created by hesheng on 14-9-30.
 */
@Service
public class FileService implements InitializingBean{

    private static final Log logger = LogFactory.getLog(FileService.class);

    private AliyunStorageService storageService = new AliyunStorageService();


    public String createFile(String fileName, byte[] fileByte) {
        try {
            storageService.storeFile(fileName, fileByte);
            return AliyunStorageService.VISTOR_URL + fileName;
        } catch (Exception e) {
            logger.error("store alliyun file is fail by filname=" + fileName);
            return "";
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
