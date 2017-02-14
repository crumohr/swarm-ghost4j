package com.example.demo.service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import org.jboss.logging.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PdfAnalyzer {

    private Logger logger = Logger.getLogger(this.getClass());

    public List<String> analyze() throws RendererException, DocumentException, IOException {

        Path filePath = Paths.get("/tmp/qr.pdf");
        PDFDocument document = new PDFDocument();

        document.load(filePath.toFile());

        SimpleRenderer renderer = new SimpleRenderer();
        renderer.setResolution(300);

        List<Image> imageList = renderer.render(document);

        // draw Image as BufferedImage
        Function<Image, BufferedImage> transformImage = i -> {
            BufferedImage bimage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(i, 0, 0, null);
            bGr.dispose();
            return bimage;
        };

        // decode qr code
        Function<BufferedImage, String> extractString = b -> {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(b)));
            try {
                return new MultiFormatReader().decode(binaryBitmap).getText();
            } catch (NotFoundException e) {
                logger.error(e);
                return null;
            }
        };

        return imageList.stream()
                .map(transformImage)
                .map(extractString)
                .filter(Objects::isNull)
                .collect(Collectors.toList());
    }
}
