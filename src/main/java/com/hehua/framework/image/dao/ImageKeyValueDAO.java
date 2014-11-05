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
public class ImageKeyValueDAO extends ReadOnlyKeyValueDAO<Long, Image> {

    @Autowired
    private ImageDAO imageDAO;

    @Override
    public Image get(Long key) {
        return imageDAO.getById(key);
    }

    @Override
    public Map<Long, Image> mget(Collection<Long> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        List<Image> images = imageDAO.getByIds(keys);
        return Transformers.transformAsOneToOneMap(images, Image.idExtractor);
    }

}
