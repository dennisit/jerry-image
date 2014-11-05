/**
 * 
 */
package com.hehua.framework.image.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehua.commons.Transformers;
import com.hehua.framework.dao.ReadOnlyKeyValueDAO;
import com.hehua.framework.image.domain.Image;

/**
 * @author zhihua
 *
 */
@Component
public class ImageByFidKeyValueDAO extends ReadOnlyKeyValueDAO<String, Image> {

    @Autowired
    private ImageDAO imageDAO;

    @Override
    public Image get(String key) {
        return imageDAO.getByFid(key);
    }

    @Override
    public Map<String, Image> mget(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        List<Image> images = imageDAO.getByFids(keys);
        return Transformers.transformAsOneToOneMap(images, Image.fidExtractor);
    }

}
