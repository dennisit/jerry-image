/**
 * 
 */
package com.hehua.framework.image.domain;

import java.util.Date;

import com.google.common.base.Function;
import com.hehua.framework.image.storage.aliyun.AliyunStorageService;

/**
 * @author zhihua
 *
 */
public class Image {

    public static final Function<Image, Long> idExtractor = new Function<Image, Long>() {

        @Override
        public Long apply(Image input) {
            return input.getId();
        }

    };

    public static final Function<Image, String> fidExtractor = new Function<Image, String>() {

        @Override
        public String apply(Image input) {
            return input.getFid();
        }

    };

    private long id;

    private String bucket;

    private String fid;

    private int width;

    private int height;

    private int size;

    private String format;

    private int status;

    private Date createTime;

    private String provider;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        // TODO
        //        switch (bucket) {
        //            case "hhimg":
        //                return "http://" + bucket + ".oss-cn-beijing.aliyuncs.com/" + fid + "." + format;
        //            default:
        return AliyunStorageService.VISTOR_URL + fid + "." + format;
        //        }
    }

    @Override
    public String toString() {
        return "Image [id=" + id + ", fid=" + fid + ", width=" + width + ", height=" + height
                + ", size=" + size + ", format=" + format + ", status=" + status + ", createTime="
                + createTime + "]";
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

}
