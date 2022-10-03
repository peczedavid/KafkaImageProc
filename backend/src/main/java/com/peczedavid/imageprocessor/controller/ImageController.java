package com.peczedavid.imageprocessor.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peczedavid.imageprocessor.dto.ImagePayload;
import com.peczedavid.imageprocessor.model.Image;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("api/process")
public class ImageController {
    
    @PostMapping("/black-and-white")
    public String processBlackAndWhite(@RequestBody ImagePayload imagePayload) {
        String src = imagePayload.getSrc();
        String metaInfo = src.split(",")[0] + ",";
        String data = src.substring(metaInfo.length(), src.length());
        String formattedData = data.replaceAll(" ", "+");
        Image image = new Image(formattedData);
        image.ProcessBlackAndWhite();
        return metaInfo + " " + image.getBase64();
    }

    @PostMapping("/grayscale")
    public String processGrayScale(@RequestBody ImagePayload imagePayload) {
        String src = imagePayload.getSrc();
        String metaInfo = src.split(",")[0] + ",";
        String data = src.substring(metaInfo.length(), src.length());
        String formattedData = data.replaceAll(" ", "+");
        Image image = new Image(formattedData);
        image.ProcessGrayScale();
        return metaInfo + " " + image.getBase64();
    }
}
