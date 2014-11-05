package com.hehua.framework.image;

import org.apache.commons.lang.StringUtils;

/**
 * Created by hesheng on 14-9-30.
 */
public class FileMisUtils {
    public static final String getMimeType(String fileName) {
        String splitFile = StringUtils.substring(fileName, fileName.lastIndexOf(".") + 1);
        switch (splitFile) {
            case "apk":
                return "application/vnd.android.package-archive";
            case "ipa":
                return "application/vnd.iphone";
        }
        throw new IllegalArgumentException("unknown format:" + fileName);
    }

}
