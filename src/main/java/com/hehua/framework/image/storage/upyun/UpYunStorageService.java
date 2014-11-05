/**
 * 
 */
package com.hehua.framework.image.storage.upyun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hehua.framework.image.domain.Image;
import com.hehua.framework.image.storage.StorageService;
import com.hehua.framework.image.storage.upyun.UpYun.PARAMS;

/**
 * @author zhihua
 *
 */
public class UpYunStorageService implements StorageService {

    private final UpYun upyun = new UpYun("hhimg", "zhouzh306", "lscbu7ey72");

    @Override
    public void store(Image image, byte[] imageBytes) throws IOException {

        // 要传到upyun后的文件路径
        String filename = image.getFid() + "." + image.getFormat();

        // 设置缩略图的参数
        Map<String, String> params = new HashMap<String, String>();

        // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
        params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());

        // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
        params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "640");

        // 设置缩略图的质量，默认 95
        params.put(PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

        // 设置缩略图的锐化，默认锐化（true）
        //        params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

        // 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
        // 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
        //        params.put(PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");

        // 上传文件，并自动创建父级目录（最多10级）
        boolean result = upyun.writeFile(filename, imageBytes, true, params);
        if (!result) {
            throw new IOException("upyun unkown reason");
        }

    }

}
