package net.orderzone.idcard.controller;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import net.orderzone.idcard.dto.BatchRequest;
import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.service.BatchService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping("/create")
    public ResponseEntity<List<ProfileResponse>> createBatch(
            @Valid @RequestBody BatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(batchService.createBatch(request));
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportAll() throws IOException, WriterException {
        byte[] zip = batchService.exportAllPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"id-cards-all.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(zip);
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> exportSelected(@RequestBody List<Long> profileIds)
            throws IOException, WriterException {
        byte[] zip = batchService.exportBatchPdf(profileIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"id-cards-batch.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(zip);
    }
}
