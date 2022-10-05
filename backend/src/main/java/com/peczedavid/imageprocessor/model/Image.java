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

            InputStream pp0 = new ByteArrayInputStream(Base64.getDecoder().decode(base64code));
            pingpong0 = ImageIO.read(pp0);
            fastRGBpp0 = new FastRGB(pingpong0);
            InputStream pp1 = new ByteArrayInputStream(Base64.getDecoder().decode(base64code));
            pingpong1 = ImageIO.read(pp1);
            fastRGBpp1 = new FastRGB(pingpong1);
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

    private int doKernel(int x, int y, int shift, FastRGB rgbReader)
    {
        double value = 0;
        int maski = 0, maskj = 0;
        for(int i = x-2; i <= (x+2); i++)
        {
            for(int j = y-2; j <= (y+2); j++)
            {
                int originalColor = (rgbReader.getRGB(i, j) >> shift) & 0xff;
                value += kernelMask[maski][maskj] * originalColor;
                maskj++;
            }
            maski++;
            maskj=0;
        }

        return (int)value;
    }

    
    public void processBlur() {
        kernelMask = new double[7][7];
        kernelMask[0] = new double[]{01, 04, 06, 04, 01};
        kernelMask[1] = new double[]{04, 16, 24, 16, 04};
        kernelMask[2] = new double[]{06, 24, 36, 24, 06};
        kernelMask[3] = new double[]{04, 16, 24, 16, 04};
        kernelMask[4] = new double[]{01, 04, 06, 04, 01};

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                kernelMask[i][j] *= 1.0 / 256.0;
            }
        }

        for(int bp = 0; bp < 5; bp++) {
            if(pingpong == 0) {
                for (int x = 0; x < pingpong0.getWidth(); x++) {
                    for (int y = 0; y < pingpong0.getHeight(); y++) {
                        int b = doKernel(x, y, 0, fastRGBpp0);
                        int g = doKernel(x, y, 8, fastRGBpp0);
                        int r = doKernel(x, y, 16, fastRGBpp0);
        
                        b = b & 0x000000ff;
                        g = (g << 8) & 0x0000ff00;
                        r = (r << 16) & 0x00ff00000;
        
                        int color = 0xff000000 | r | g | b;
                        fastRGBpp1.setRGB(x, y, color);
                    }
                }
            }
            else if(pingpong == 1) {
                for (int x = 0; x < pingpong1.getWidth(); x++) {
                    for (int y = 0; y < pingpong1.getHeight(); y++) {
                        int b = doKernel(x, y, 0, fastRGBpp1);
                        int g = doKernel(x, y, 8, fastRGBpp1);
                        int r = doKernel(x, y, 16, fastRGBpp1);
        
                        b = b & 0x000000ff;
                        g = (g << 8) & 0x0000ff00;
                        r = (r << 16) & 0x00ff00000;
        
                        int color = 0xff000000 | r | g | b;
                        fastRGBpp0.setRGB(x, y, color);
                    }
                }
            }
            pingpong = (pingpong + 1) % 2;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            if(pingpong == 0)
                ImageIO.write(pingpong0, "png", os);
            else if(pingpong == 1)
                ImageIO.write(pingpong1, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modifiedBase64 = Base64.getEncoder().encodeToString(os.toByteArray());
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

    private BufferedImage pingpong0, pingpong1;
    private BufferedImage image;
    private FastRGB fastRGB;
    private FastRGB fastRGBpp0, fastRGBpp1;
    private String modifiedBase64;

    private double[][] kernelMask;
    private int pingpong = 0;
}
