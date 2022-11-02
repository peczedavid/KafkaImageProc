package com.peczedavid.imageprocessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peczedavid.imageprocessor.dto.ImagePayload;
import com.peczedavid.imageprocessor.dto.ImageResponse;
import com.peczedavid.imageprocessor.service.ImageService;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("api/process")
public class ImageController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/black-and-white")
    public ResponseEntity<ImageResponse> processBlackAndWhite(@RequestBody ImagePayload imagePayload) {
        kafkaTemplate.send("image-response-topic", "Black and white image processed.");
        imageService.processBlackAndWhite(imagePayload);
        return new ResponseEntity<ImageResponse>(imageService.createResponse(), HttpStatus.OK);
    }

    @PostMapping("/grayscale")
    public ResponseEntity<ImageResponse> processGrayScale(@RequestBody ImagePayload imagePayload) {
        kafkaTemplate.send("image-response-topic", "Grayscale image processed.");
        imageService.processGrayScale(imagePayload);
        return new ResponseEntity<ImageResponse>(imageService.createResponse(), HttpStatus.OK);
    }

    @PostMapping("/contrast")
    public ResponseEntity<ImageResponse> processContrast(@RequestBody ImagePayload imagePayload) {
        kafkaTemplate.send("image-response-topic", "Contrast image processed.");
        imageService.processContrast(imagePayload);
        return new ResponseEntity<ImageResponse>(imageService.createResponse(), HttpStatus.OK);
    }

    @PostMapping("/blur")
    public ResponseEntity<ImageResponse> processBlur(@RequestBody ImagePayload imagePayload) {
        kafkaTemplate.send("image-response-topic", "Blur image processed.");
        imageService.processBlur(imagePayload);
        return new ResponseEntity<ImageResponse>(imageService.createResponse(), HttpStatus.OK);
    }

    @Autowired
    private ImageService imageService;
}
