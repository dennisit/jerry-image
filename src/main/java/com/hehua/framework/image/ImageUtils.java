/**
 * 
 */
package com.hehua.framework.image;

import java.awt.Dimension;

import magick.MagickImage;
import magick.PixelPacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageUtils {

    private static final Log logger = LogFactory.getLog(ImageUtils.class);

    public static final String getBucketName() {
        return "abtest";
    }

    public static final String getMimeType(String format) {
        switch (format) {
            case "webp":
                return "image/webp";
            case "gif":
                return "image/gif";
            case "png":
                return "image/png";
            case "jpeg":
                return "image/jpeg";
        }
        throw new IllegalArgumentException("unknown format:" + format);
    }

    /**
     * 计算平均亮度
     * 
     * @param mi
     * @return
     */
    public static double getAvgLuminance(MagickImage mi) {
        try {
            Dimension dimension = mi.getDimension();
            int width = (int) dimension.getWidth();
            int height = (int) dimension.getHeight();
            double totalLumiance = 0;

            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    PixelPacket onePixel = mi.getOnePixel(w, h);
                    int red = onePixel.getRed() / 255;
                    int green = onePixel.getGreen() / 255;
                    int blue = onePixel.getBlue() / 255;

                    double luminance = 0.299 * red + 0.587 * green + 0.114 * blue;

                    totalLumiance += luminance;
                }
            }
            return totalLumiance / (width * height);
        } catch (Exception e) {
            logger.error("Ops.", e);
            e.printStackTrace();
            return 0;
        }
    }
}
