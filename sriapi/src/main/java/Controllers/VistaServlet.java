package Controllers;

import dao.VistaConsultaDetalleDAO;
import dao.impl.VistaConsultaDetalleDAOImpl;
import models.VistaConsultaDetalle;
import utils.PdfGenerator;
import utils.ExcelGenerator;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class VistaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ruc = request.getParameter("ruc"); // Obtener el RUC desde la solicitud

        // Verificar si el RUC fue proporcionado
        if (ruc == null || ruc.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "RUC es obligatorio");
            return;
        }

        try {
            Connection connection = (Connection) request.getAttribute("connection");
            VistaConsultaDetalleDAO dao = new VistaConsultaDetalleDAOImpl();

            // Obtener detalles filtrados por RUC
            List<VistaConsultaDetalle> detalles = ((VistaConsultaDetalleDAOImpl) dao).getDetallesPorRuc(connection, ruc);

            // Verificar si se encontraron resultados
            if (detalles.isEmpty()) {
                response.getWriter().write("No se encontraron resultados para el RUC proporcionado.");
                return;
            }

            // Exportar a PDF
            PdfGenerator.generatePdf("vista_consulta_" + ruc + ".pdf", detalles.toString());

            // Exportar a Excel
            ExcelGenerator.generateExcel("vista_consulta_" + ruc + ".xlsx", detalles);

            // Devolver la respuesta en formato JSON
            response.setContentType("application/json");
            response.getWriter().write(detalles.toString());

        } catch (Exception e) {
            // Manejo de errores
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
