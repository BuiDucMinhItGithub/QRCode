package com.example.qrcode.controller;

import com.example.qrcode.service.QRCodeService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/qrcode")
public class ApiQR {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping
    public ResponseEntity<byte[]> generate(@RequestParam(name = "text") String text,
                                           @RequestParam(name = "width") int width,
                                           @RequestParam(name = "height") int height) {
        byte[] qrImage = qrCodeService.generate(text, width, height);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }

    @PostMapping
    public String addAttachments(@RequestParam("attachments") MultipartFile multipartFiles) throws NotFoundException, IOException {
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(multipartFiles.getBytes());
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(bufferedImageLuminanceSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            Result result = multiFormatReader.decode(binaryBitmap);
            System.out.println(multipartFiles.getSize());
            return result.getText();
        }else {
            return "failed to read this qr code";
        }
    }
}
