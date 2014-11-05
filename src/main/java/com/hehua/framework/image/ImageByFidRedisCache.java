/**
 * 
 */
package com.hehua.framework.image;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hehua.framework.cache.AbstractRedisCache;
import com.hehua.framework.image.domain.Image;
import com.hehua.framework.jedis.PoolableJedisManager;

/**
 * @author zhihua
 *
 */
@Component
public class ImageByFidRedisCache extends AbstractRedisCache<String, Image> {

    public ImageByFidRedisCache() {
        super(PoolableJedisManager.getDefaultCacheJedis());
    }

    @Override
    public String buildKey(String key) {
        return "image_by_fid:" + key;
    }

    @Override
    public String encode(Image object) {
        return JSON.toJSONString(object);
    }

    @Override
    public Image decode(String text) {
        return JSON.parseObject(text, Image.class);
    }

}
