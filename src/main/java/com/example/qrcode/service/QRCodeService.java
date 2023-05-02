package com.example.qrcode.service;

import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {

    public byte[] generate(String text, int width, int height) {

        try (ByteArrayOutputStream bos = QRCode.from(text).withSize(width, height).stream(); ) {

            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
