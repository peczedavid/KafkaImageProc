package com.peczedavid.imageprocessor.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class FastRGB {

    FastRGB(BufferedImage image) {

        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
        height = image.getHeight();
        image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixelLength = 3;
        if (hasAlphaChannel) {
            pixelLength = 4;
        }
    }

    public int getRGB(int x, int y) {
        int pos = (y * pixelLength * width) + (x * pixelLength);
        int argb = -16777216; // 255 alpha

        if(pos > (pixels.length - pixelLength) || pos < 0)
            return argb;

        if (hasAlphaChannel) {
            argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
        }

        argb += ((int) pixels[pos++] & 0xff); // blue
        argb += (((int) pixels[pos++] & 0xff) << 8); // green
        argb += (((int) pixels[pos++] & 0xff) << 16); // red
        return argb;
    }

    public void setRGB(int x, int y, int rgb) {
        int pos = (y * pixelLength * width) + (x * pixelLength);

        pixels[pos++] = (byte) (rgb & 0xff);
        pixels[pos++] = (byte) ((rgb >> 8) & 0xff);
        pixels[pos++] = (byte) ((rgb >> 16) & 0xff);
    }

    public int getPixelLength() {
        return pixelLength;
    }

    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int pixelLength;
    private byte[] pixels;
    
}
