package com.vansh.file.controller;

import com.vansh.file.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    @Autowired
    private ImageService service;

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = service.uploadFile(file);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping("/downloadImage/{image}")
    public ResponseEntity<?> downloadImage(@PathVariable String image){
        byte[] b = service.downloadImage(image);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(b);
    }
}
