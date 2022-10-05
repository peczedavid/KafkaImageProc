package com.peczedavid.imageprocessor.model;

import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// TODO: don't hardcode PNG
public class Image {

    public Image(String base64code) {
        try {
            InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(base64code));
            image = ImageIO.read(is);
            fastRGB = new FastRGB(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processGrayScale() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = fastRGB.getRGB(x, y);
                int b = rgb & 0xff;
                int g = (rgb >> 8) & 0xff;
                int r = (rgb >> 16) & 0xff;
                byte value = (byte) ((b + g + r) / 3);

                b = value & 0x000000ff;
                g = (value << 8) & 0x0000ff00;
                r = (value << 16) & 0x00ff00000;

                int grayscale = 0xff000000 | r | g | b;
                fastRGB.setRGB(x, y, grayscale);
            }
        }

        writeBase64();
    }

    public void processBlackAndWhite() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = fastRGB.getRGB(x, y);
                int b = rgb & 0xff;
                int g = (rgb >> 8) & 0xff;
                int r = (rgb >> 16) & 0xff;
                byte value = (byte) ((b + g + r) / 3);
                if (value > 0)
                    value = -1;
                else
                    value = 0;

                b = value & 0x000000ff;
                g = (value << 8) & 0x0000ff00;
                r = (value << 16) & 0x00ff00000;

                int blackAndWhite = 0xff000000 | r | g | b;
                fastRGB.setRGB(x, y, blackAndWhite);
            }
        }

        writeBase64();
    }

    public void processContrast() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = fastRGB.getRGB(x, y);
                int b = rgb & 0xff;
                int g = (rgb >> 8) & 0xff;
                int r = (rgb >> 16) & 0xff;
                
                double bDouble = (double)b/255.0;
                double gDouble = (double)g/255.0;
                double rDouble = (double)r/255.0;

                bDouble = clamp(bDouble*bDouble*(4.0 - 3.0*bDouble), 0.0, 1.0);
                gDouble = clamp(gDouble*gDouble*(4.0 - 3.0*gDouble), 0.0, 1.0);
                rDouble = clamp(rDouble*rDouble*(4.0 - 3.0*rDouble), 0.0, 1.0);

                b = (int)(bDouble*255.0);
                g = (int)(gDouble*255.0);
                r = (int)(rDouble*255.0);

                b = b & 0x000000ff;
                g = (g << 8) & 0x0000ff00;
                r = (r << 16) & 0x00ff00000;

                int color = 0xff000000 | r | g | b;
                fastRGB.setRGB(x, y, color);
            }
        }

        writeBase64();
    }

    private void writeBase64() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modifiedBase64 = Base64.getEncoder().encodeToString(os.toByteArray());
    }

    public String getBase64() {
        return modifiedBase64;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private BufferedImage image;
    private FastRGB fastRGB;
    private String modifiedBase64;
}
