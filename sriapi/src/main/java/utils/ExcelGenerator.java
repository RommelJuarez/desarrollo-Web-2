package utils;

import models.VistaConsultaDetalle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    public static void generateExcel(String filename, String[] headers, String[][] data) throws IOException {
        FileWriter writer = new FileWriter(filename);

        // Escribir encabezados
        for (String header : headers) {
            writer.append(header).append(",");
        }
        writer.append("\n");

        // Escribir los datos
        for (String[] row : data) {
            for (String cell : row) {
                writer.append(cell).append(",");
            }
            writer.append("\n");
        }

        writer.flush();
        writer.close();
    }

    public static void generateExcel(String s, List<VistaConsultaDetalle> detalles) {
    }
}
