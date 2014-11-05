/**
 * 
 */
package com.hehua.framework.image.storage;

import java.io.IOException;

import com.hehua.framework.image.domain.Image;

/**
 * @author zhihua
 *
 */
public interface StorageService {

    public void store(Image image, byte[] imageBytes) throws IOException;
}
