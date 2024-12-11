package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static void generatePdf(String filename, String content) throws IOException {
        // Crear imagen en blanco para representar el "PDF"
        BufferedImage image = new BufferedImage(500, 700, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Fondo blanco
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Establecer color de texto
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 16));

        // Dibujar el contenido
        graphics.drawString(content, 20, 50);  // Este es solo un ejemplo simple.

        // Guardar como archivo PDF (en realidad, se guardará como una imagen)
        File file = new File(filename);
        try (FileOutputStream out = new FileOutputStream(file)) {
            // Guardamos la imagen como un archivo JPEG (puedes cambiar el formato según lo que desees)
            javax.imageio.ImageIO.write(image, "jpg", out);  // En realidad, no es un PDF real
        }

        graphics.dispose();
    }
}
