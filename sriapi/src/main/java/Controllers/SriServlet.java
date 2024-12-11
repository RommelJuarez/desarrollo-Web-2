package Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Properties;

@WebServlet({"/consultar", "/consultar/generar-pdf", "/consultar/generar-excel"})
public class SriServlet extends HttpServlet {
    private String authToken;
    private String cookieValues;

    @Override
    public void init() throws ServletException {
        // Cargar las propiedades desde el archivo config.properties
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new ServletException("Archivo config.properties no encontrado.");
            }
            properties.load(input);
            authToken = properties.getProperty("auth.token");
            cookieValues = properties.getProperty("cookie.values");
        } catch (IOException e) {
            throw new ServletException("Error al cargar el archivo config.properties", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ruc = request.getParameter("ruc");

        if (ruc == null || ruc.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Se requiere el parámetro RUC\"}");
            return;
        }

        try {
            // Configurar la conexión HTTP al servicio externo
            URL url = new URL("https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/obtenerPorNumerosRuc?ruc=" + ruc);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authToken);
            connection.setRequestProperty("Cookie", cookieValues);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                // Leer la respuesta del servicio externo
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNextLine()) {
                    jsonResponse.append(scanner.nextLine());
                }
                scanner.close();

                // Almacenar los datos JSON para generar PDF o Excel
                String jsonData = jsonResponse.toString();

                // Verificar si la solicitud es para descargar el PDF
                if (request.getRequestURI().endsWith("/generar-pdf")) {
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment; filename=\"sri_consulta.pdf\"");
                    generatePdf(response.getOutputStream(), jsonData);
                    return;
                }

                // Verificar si la solicitud es para descargar el Excel
                if (request.getRequestURI().endsWith("/generar-excel")) {
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment; filename=\"sri_consulta.xlsx\"");
                    generateExcel(response.getOutputStream(), jsonData);
                    return;
                }

                // Si no es una solicitud de descarga, devolver la respuesta JSON estándar
                response.setContentType("application/json");
                response.getWriter().write(jsonData);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Error al consultar el SRI\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Error al consultar el SRI\"}");
        }
    }

    // Método para generar PDF sin dependencias externas
    private void generatePdf(OutputStream outputStream, String jsonData) throws IOException {
        // Crear un archivo PDF simple con solo texto básico
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write("%PDF-1.4\n");
        writer.write("1 0 obj\n");
        writer.write("<< /Type /Catalog /Pages 2 0 R >>\n");
        writer.write("endobj\n");
        writer.write("2 0 obj\n");
        writer.write("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n");
        writer.write("endobj\n");
        writer.write("3 0 obj\n");
        writer.write("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R >>\n");
        writer.write("endobj\n");
        writer.write("4 0 obj\n");
        writer.write("<< /Length 44 >>\n");
        writer.write("stream\n");
        writer.write("BT\n/F1 12 Tf\n100 700 Td\n(" + jsonData + ") Tj\nET\n");
        writer.write("endstream\n");
        writer.write("endobj\n");
        writer.write("xref\n");
        writer.write("0 5\n");
        writer.write("0000000000 65535 f \n");
        writer.write("0000000010 00000 n \n");
        writer.write("0000000066 00000 n \n");
        writer.write("0000000110 00000 n \n");
        writer.write("0000000200 00000 n \n");
        writer.write("trailer\n");
        writer.write("<< /Root 1 0 R /Size 5 >>\n");
        writer.write("startxref\n");
        writer.write("300\n");
        writer.write("%%EOF\n");
        writer.flush();
    }

    // Método para generar Excel sin dependencias externas
    private void generateExcel(OutputStream outputStream, String jsonData) throws IOException {
        // Crear un archivo Excel simple (formato XML para .xlsx)
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");
        writer.write("<sheetData>\n");
        writer.write("<row>\n");
        writer.write("<c r=\"A1\" t=\"inlineStr\">\n");
        writer.write("<is>\n");
        writer.write("<t>" + jsonData + "</t>\n");
        writer.write("</is>\n");
        writer.write("</c>\n");
        writer.write("</row>\n");
        writer.write("</sheetData>\n");
        writer.write("</workbook>\n");
        writer.flush();
    }
}
