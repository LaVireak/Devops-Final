package net.orderzone.idcard.service;

import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PdfExportService {

    private static final float CARD_WIDTH  = 242f;
    private static final float CARD_HEIGHT = 153f;

    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;

    @Value("${app.photo-dir:./uploads/photos}")
    private String photoDir;

    public PdfExportService(QrCodeService qrCodeService, BarcodeService barcodeService) {
        this.qrCodeService = qrCodeService;
        this.barcodeService = barcodeService;
    }

    public byte[] exportSinglePdf(Profile profile) throws IOException, WriterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PageSize cardSize = new PageSize(CARD_WIDTH, CARD_HEIGHT);
        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdf    = new PdfDocument(writer);
             Document document  = new Document(pdf, cardSize)) {
            document.setMargins(0, 0, 0, 0);
            renderCard(document, profile);
        }
        return out.toByteArray();
    }

    public byte[] exportBatchZip(List<Profile> profiles) throws IOException, WriterException {
        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(zipOut)) {
            for (Profile profile : profiles) {
                byte[] pdfBytes = exportSinglePdf(profile);
                String entry = profile.getRegistrationNumber().replace("/", "_") + ".pdf";
                zip.putNextEntry(new ZipEntry(entry));
                zip.write(pdfBytes);
                zip.closeEntry();
            }
        }
        return zipOut.toByteArray();
    }

    private void renderCard(Document doc, Profile profile) throws IOException, WriterException {
        Template tpl = profile.getTemplate();
        DeviceRgb primary   = hexToRgb(tpl != null ? tpl.getPrimaryColor()   : "#1d4ed8");
        DeviceRgb secondary = hexToRgb(tpl != null ? tpl.getSecondaryColor() : "#e0e7ff");
        DeviceRgb text      = hexToRgb(tpl != null ? tpl.getTextColor()      : "#111827");
        String orgName  = (tpl != null && tpl.getOrganizationName() != null) ? tpl.getOrganizationName() : "ID CARD";
        String tagline  = tpl != null ? tpl.getTagline() : null;

        // Header
        Table header = new Table(UnitValue.createPercentArray(new float[]{1}))
                .useAllAvailableWidth().setBackgroundColor(primary);
        Cell hCell = new Cell()
                .add(new Paragraph(orgName).setFontColor(ColorConstants.WHITE).setBold()
                        .setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER).setPaddingTop(5).setPaddingBottom(2);
        if (tagline != null && !tagline.isBlank()) {
            hCell.add(new Paragraph(tagline).setFontColor(secondary).setFontSize(6)
                    .setTextAlignment(TextAlignment.CENTER));
        }
        header.addCell(hCell.setPaddingBottom(4));
        doc.add(header);

        // Body
        Table body = new Table(UnitValue.createPercentArray(new float[]{38, 62}))
                .useAllAvailableWidth().setBackgroundColor(secondary);

        Cell photoCell = new Cell().setBorder(Border.NO_BORDER).setPadding(6);
        if (profile.hasPhoto()) {
            try {
                byte[] photoBytes = Files.readAllBytes(Paths.get(photoDir).resolve(profile.getPhotoFileName()));
                photoCell.add(new Image(ImageDataFactory.create(photoBytes))
                        .setWidth(55).setHeight(65).setHorizontalAlignment(HorizontalAlignment.CENTER));
            } catch (IOException ignored) {
                photoCell.add(new Paragraph("[No Photo]").setFontSize(6).setFontColor(text));
            }
        } else {
            photoCell.add(new Paragraph("[No Photo]").setFontSize(6).setFontColor(text));
        }
        body.addCell(photoCell);

        Cell info = new Cell().setBorder(Border.NO_BORDER).setPadding(6);
        info.add(new Paragraph(profile.getFullName()).setBold().setFontSize(9).setFontColor(primary));
        if (profile.getTitle() != null)      info.add(line(profile.getTitle(), 7, text));
        if (profile.getDepartment() != null) info.add(line("Dept: " + profile.getDepartment(), 7, text));
        info.add(line("ID: " + profile.getRegistrationNumber(), 7, text));
        if (profile.getEmail() != null)      info.add(line(profile.getEmail(), 6, text));
        info.add(line(profile.getType().name(), 7, primary));
        body.addCell(info);
        doc.add(body);

        // Footer
        Table footer = new Table(UnitValue.createPercentArray(new float[]{45, 55}))
                .useAllAvailableWidth().setBackgroundColor(ColorConstants.WHITE);

        Cell qrCell = new Cell().setBorder(Border.NO_BORDER).setPadding(3)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            byte[] qr = qrCodeService.generateVerificationQr(profile.getUuid());
            qrCell.add(new Image(ImageDataFactory.create(qr)).setWidth(32).setHeight(32)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        } catch (Exception e) {
            qrCell.add(new Paragraph("QR").setFontSize(6));
        }
        footer.addCell(qrCell);

        Cell bcCell = new Cell().setBorder(Border.NO_BORDER).setPadding(3)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        try {
            byte[] bc = barcodeService.generateBarcode(
                    profile.getRegistrationNumber(), profile.getBarcodeType(), 110, 28);
            bcCell.add(new Image(ImageDataFactory.create(bc)).setWidth(100).setHeight(25)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        } catch (Exception e) {
            bcCell.add(new Paragraph(profile.getRegistrationNumber()).setFontSize(6));
        }
        footer.addCell(bcCell);
        doc.add(footer);
    }

    private Paragraph line(String text, float size, DeviceRgb color) {
        return new Paragraph(text).setFontSize(size).setFontColor(color).setMargin(0);
    }

    private DeviceRgb hexToRgb(String hex) {
        try {
            hex = hex.trim().replace("#", "");
            return new DeviceRgb(
                    Integer.parseInt(hex.substring(0, 2), 16),
                    Integer.parseInt(hex.substring(2, 4), 16),
                    Integer.parseInt(hex.substring(4, 6), 16));
        } catch (Exception e) {
            return new DeviceRgb(29, 78, 216);
        }
    }
}
