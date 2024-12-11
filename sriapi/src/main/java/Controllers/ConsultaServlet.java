package Controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/consultarbase")
public class ConsultaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtenemos la conexión guardada en el request por el filtro
        Connection conn = (Connection) request.getAttribute("dbConnection");

        try {
            // Preparamos la consulta SQL para obtener los resultados de la vista
            String query = "SELECT * FROM vista_consulta_detalle";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Comenzamos a generar el HTML para la tabla
            StringBuilder htmlResponse = new StringBuilder();
            htmlResponse.append("<table border='1'>")
                    .append("<tr><th>RUC</th><th>Razón Social</th><th>Estado</th><th>Actividad</th><th>Tipo</th><th>Régimen</th></tr>");

            // Iteramos sobre los resultados y los agregamos a la tabla HTML
            while (rs.next()) {
                htmlResponse.append("<tr>")
                        .append("<td>").append(rs.getString("numeroRuc")).append("</td>")
                        .append("<td>").append(rs.getString("razonSocial")).append("</td>")
                        .append("<td>").append(rs.getString("estadoContribuyenteRuc")).append("</td>")
                        .append("<td>").append(rs.getString("actividadEconomicaPrincipal")).append("</td>")
                        .append("<td>").append(rs.getString("tipoContribuyente")).append("</td>")
                        .append("<td>").append(rs.getString("regimen")).append("</td>")
                        .append("</tr>");
            }

            htmlResponse.append("</table>");

            // Establecemos la respuesta como HTML
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Resultados de la Consulta SRI</h1>");
            out.println(htmlResponse.toString());
            out.println("</body></html>");
        } catch (SQLException e) {
            response.getWriter().println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
}
