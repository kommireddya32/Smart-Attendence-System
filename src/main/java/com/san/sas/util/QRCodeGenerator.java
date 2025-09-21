package com.san.sas.util; // Create this new package

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeGenerator {

    /**
     * Generates a QR code image from a given text.
     * @param text The data to be encoded in the QR code.
     * @param width The desired width of the image.
     * @param height The desired height of the image.
     * @return A byte array representing the QR code as a PNG image.
     * @throws WriterException If there's an error during QR code creation.
     * @throws IOException If there's an error writing the image to the byte stream.
     */
    public static byte[] generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        
        return pngOutputStream.toByteArray();
    }
}