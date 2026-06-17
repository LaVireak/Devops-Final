package net.orderzone.idcard.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import net.orderzone.idcard.model.BarcodeType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

@Service
public class BarcodeService {

    public byte[] generateBarcode(String data, BarcodeType type, int width, int height)
            throws WriterException, IOException {
        BarcodeFormat format = (type == BarcodeType.EAN_13)
                ? BarcodeFormat.EAN_13 : BarcodeFormat.CODE_128;

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 1);

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix = writer.encode(data, format, width, height, hints);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}
