package com.peczedavid.imageprocessor.service;

import org.springframework.stereotype.Service;

import com.peczedavid.imageprocessor.dto.ImagePayload;
import com.peczedavid.imageprocessor.dto.ImageResponse;
import com.peczedavid.imageprocessor.model.Image;

@Service
public class ImageService {

    public ImageResponse createResponse() {
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setBase64(metaInfo + " " + image.getBase64());
        imageResponse.setTimeMillis((int) processTimeMillis);
        return imageResponse;
    }

    public void processBlackAndWhite(ImagePayload imagePayload) {
        long startTimeMillis = System.currentTimeMillis();
        processImagePayload(imagePayload);
        image.processBlackAndWhite();
        processTimeMillis = System.currentTimeMillis() - startTimeMillis;
    }

    public void processGrayScale(ImagePayload imagePayload) {
        long startTimeMillis = System.currentTimeMillis();
        processImagePayload(imagePayload);
        image.processGrayScale();
        processTimeMillis = System.currentTimeMillis() - startTimeMillis;
    }

    public void processContrast(ImagePayload imagePayload) {
        long startTimeMillis = System.currentTimeMillis();
        processImagePayload(imagePayload);
        image.processContrast();
        processTimeMillis = System.currentTimeMillis() - startTimeMillis;
    }

    public void processBlur(ImagePayload imagePayload) {
        long startTimeMillis = System.currentTimeMillis();
        processImagePayload(imagePayload);
        image.processBlur();
        processTimeMillis = System.currentTimeMillis() - startTimeMillis;
    }

    private void processImagePayload(ImagePayload imagePayload) {
        String src = imagePayload.getSrc();
        metaInfo = imagePayload.getSrc().split(",")[0] + ",";
        String data = src.substring(metaInfo.length(), src.length());
        String formattedData = data.replaceAll(" ", "+");
        image = new Image(formattedData);
    }

    // Data about last process
    private String metaInfo;
    private Image image;
    private long processTimeMillis;

}
