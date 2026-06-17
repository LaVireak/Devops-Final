package net.orderzone.idcard.service;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class QrCodeServiceTest {

    private final QrCodeService qrCodeService = new QrCodeService();

    @Test
    void generateQrCode_shouldReturnNonEmptyPngBytes() throws WriterException, IOException {
        byte[] result = qrCodeService.generateQrCode("https://example.com/verify/abc123", 200);

        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
        // PNG magic bytes: 0x89 0x50 0x4E 0x47
        assertThat(result[0] & 0xFF).isEqualTo(0x89);
        assertThat(result[1] & 0xFF).isEqualTo(0x50);
        assertThat(result[2] & 0xFF).isEqualTo(0x4E);
        assertThat(result[3] & 0xFF).isEqualTo(0x47);
    }

    @Test
    void generateQrCode_shouldProduceDifferentSizesCorrectly()
            throws WriterException, IOException {
        byte[] small = qrCodeService.generateQrCode("test", 100);
        byte[] large = qrCodeService.generateQrCode("test", 400);
        assertThat(large.length).isGreaterThan(small.length);
    }
}
