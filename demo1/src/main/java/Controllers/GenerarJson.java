package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Productos;
import services.ProductoService;
import services.ProductoServiceImplement;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/generar")
public class GenerarJson extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductoService service = new ProductoServiceImplement();
        List<Productos> productos = service.listar();

        // Configurar la respuesta como JSON
        resp.setContentType("application/json;charset=UTF-8");

        // Generar JSON
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < productos.size(); i++) {
            Productos p = productos.get(i);
            json.append("{");
            json.append("\"idProducto\":").append(p.getIdProducto()).append(",");
            json.append("\"nombreProducto\":\"").append(p.getNombreProducto()).append("\",");
            json.append("\"categoria\":\"").append(p.getCategoria()).append("\",");
            json.append("\"precioProducto\":").append(p.getPrecioProducto());
            json.append("}");
            if (i < productos.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        // Escribir la respuesta
        PrintWriter out = resp.getWriter();
        out.print(json.toString());
    }
}