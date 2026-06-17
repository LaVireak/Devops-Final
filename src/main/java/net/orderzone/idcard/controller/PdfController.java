package net.orderzone.idcard.controller;

import com.google.zxing.WriterException;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.service.BarcodeService;
import net.orderzone.idcard.service.PdfExportService;
import net.orderzone.idcard.service.ProfileService;
import net.orderzone.idcard.service.QrCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PdfController {

    private final ProfileService profileService;
    private final PdfExportService pdfExportService;
    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;

    public PdfController(ProfileService profileService,
                          PdfExportService pdfExportService,
                          QrCodeService qrCodeService,
                          BarcodeService barcodeService) {
        this.profileService = profileService;
        this.pdfExportService = pdfExportService;
        this.qrCodeService = qrCodeService;
        this.barcodeService = barcodeService;
    }

    @GetMapping("/profiles/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id)
            throws IOException, WriterException {
        Profile profile = profileService.findById(id);
        byte[] pdf = pdfExportService.exportSinglePdf(profile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + profile.getRegistrationNumber() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/profiles/{id}/qrcode")
    public ResponseEntity<byte[]> qrCode(@PathVariable Long id)
            throws IOException, WriterException {
        Profile profile = profileService.findById(id);
        byte[] qr = qrCodeService.generateVerificationQr(profile.getUuid());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qr);
    }

    @GetMapping("/profiles/{id}/barcode")
    public ResponseEntity<byte[]> barcode(@PathVariable Long id)
            throws IOException, WriterException {
        Profile profile = profileService.findById(id);
        byte[] bc = barcodeService.generateBarcode(
                profile.getRegistrationNumber(), profile.getBarcodeType(), 300, 80);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bc);
    }
}
