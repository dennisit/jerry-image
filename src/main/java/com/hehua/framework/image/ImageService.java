/**
 * 
 */
package com.hehua.framework.image;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hehua.framework.dao.DAOUtils;
import com.hehua.framework.dao.MultilayerKeyValueDAO;
import com.hehua.framework.image.dao.ImageByFidKeyValueDAO;
import com.hehua.framework.image.dao.ImageDAO;
import com.hehua.framework.image.dao.ImageKeyValueDAO;
import com.hehua.framework.image.domain.Image;
import com.hehua.framework.image.storage.StorageService;
import com.hehua.framework.image.storage.aliyun.AliyunStorageService;

/**
 * @author zhihua
 *
 */
@Service
public class ImageService implements InitializingBean {

    private static final Log logger = LogFactory.getLog(ImageService.class);

    private StorageService storageService = new AliyunStorageService();

    //    private StorageService storageService = new UpYunStorageService();

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private ImageRedisCache imageCache;

    @Autowired
    private ImageByFidRedisCache imageByFidRedisCache;

    @Autowired
    private ImageKeyValueDAO imageKeyValueDAO;

    @Autowired
    private ImageByFidKeyValueDAO imageByFidKeyValueDAO;

    private MultilayerKeyValueDAO<Long, Image> imageMulDAO;

    private MultilayerKeyValueDAO<String, Image> imageByFidMulDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        imageMulDAO = DAOUtils.multiDAO(imageCache, imageKeyValueDAO);
        imageByFidMulDAO = DAOUtils.multiDAO(imageByFidRedisCache, imageByFidKeyValueDAO);
    }

    public Image createImage(byte[] imageData) {
        return createImage("hhimg", imageData);
    }

    private Image createImage(String bucket, byte[] imageData) {
        Image image = newImage(bucket, imageData);
        try {
            storageService.store(image, imageData);

            imageDAO.insert(image);
            imageMulDAO.set(image.getId(), image);
            return image;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bucket
     * @param imageData
     * @return
     */
    private Image newImage(String bucket, byte[] imageData) {
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

        Image image = new Image();
        image.setId(0);
        image.setFid(UUID.randomUUID().toString());
        image.setFormat(format);
        image.setWidth(width);
        image.setHeight(height);
        image.setSize(imageData.length);
        image.setCreateTime(new Date());
        image.setStatus(0);
        image.setBucket(bucket);
        return image;
    }

    public Image getImageById(long imageId) {
        //        return this.imageDAO.getById(imageId);
        return imageMulDAO.get(imageId);
    }

    public Image getImageByFid(String fid) {
        return imageByFidMulDAO.get(fid);
    }

    public Map<String, Image> getImagesByFid(Collection<String> fids) {
        //        if (CollectionUtils.isEmpty(imageIds)) {
        //            return Collections.emptyMap();
        //        }
        //
        //        List<Image> images = imageDAO.getByIds(imageIds);
        //        return Transformers.transformAsOneToOneMap(images, Image.idExtractor);
        return imageByFidMulDAO.mget(fids);
    }

    public Map<Long, Image> getImagesById(Collection<Long> imageIds) {
        //        if (CollectionUtils.isEmpty(imageIds)) {
        //            return Collections.emptyMap();
        //        }
        //
        //        List<Image> images = imageDAO.getByIds(imageIds);
        //        return Transformers.transformAsOneToOneMap(images, Image.idExtractor);
        return imageMulDAO.mget(imageIds);
    }

    public static void main(String[] args) throws IOException, MagickException {

        byte[] imageData = FileUtils.readFileToByteArray(new File(
                "/Users/hesheng/workspace/tmp/f68807475c95d7a49cd1da54672c715d.jpg"));

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath*:/spring/applicationContext*.xml");

        ImageService imageService = applicationContext.getBean(ImageService.class);
        Image image = imageService.createImage(imageData);
        System.out.println(image.getId());

        System.out.println(imageService.getImageById(125));

        System.out.println(imageService.getImageById(125));

        System.out.println(imageService.getImagesById(Lists.<Long> newArrayList(1L, 2L, 3L, 4L,
                100L, 102L, 125L, 126L)));

    }

}
