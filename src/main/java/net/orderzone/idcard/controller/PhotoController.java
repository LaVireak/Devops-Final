package net.orderzone.idcard.controller;

import net.orderzone.idcard.service.PhotoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profiles/{id}/photo")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<String> upload(@PathVariable Long id,
                                          @RequestParam("file") MultipartFile file)
            throws IOException {
        String fileName = photoService.savePhoto(id, file);
        return ResponseEntity.ok("Photo saved: " + fileName);
    }

    @GetMapping
    public ResponseEntity<byte[]> get(@PathVariable Long id) throws IOException {
        byte[] bytes = photoService.getPhotoBytes(id);
        String ct = photoService.getContentType(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, ct)
                .body(bytes);
    }
}
