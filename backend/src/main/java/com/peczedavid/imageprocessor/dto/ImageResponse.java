package com.peczedavid.imageprocessor.dto;

import lombok.Data;

@Data
public class ImageResponse {
    
    private String base64;
    private int timeMillis;

}
