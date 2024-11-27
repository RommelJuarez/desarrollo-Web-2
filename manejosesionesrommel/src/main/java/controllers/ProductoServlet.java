package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Productos;
import services.LoginService;
import services.LoginServiceSessionImplement;
import services.ProductoService;
import services.ProductoServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductoService servicios = new ProductoServiceImplement();
        List<Productos> productos = servicios.listar();

        HttpSession session = req.getSession();
        List<Productos> carrito = (List<Productos>) session.getAttribute("carrito");

        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"utf-8\">");
            out.println("<title>Productos</title>");
            out.println("<link rel='stylesheet' href='" + req.getContextPath() + "/styles/index.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1 class='title'>Listado de Productos</h1>");

            if (usernameOptional.isPresent()) {
                out.println("<div class='welcome-message'> Hola " + usernameOptional.get() + " Bienvenido</div>");
            }

            out.println("<table class='product-table'>");
            out.println("<tr>");
            out.println("<th>ID Producto</th>");
            out.println("<th>Nombre</th>");
            out.println("<th>Categoría</th>");
            if (usernameOptional.isPresent()) {
                out.println("<th>Precio</th>");
                out.println("<th>Acciones</th>");
            }
            out.println("</tr>");
            productos.forEach(pr -> {
                out.println("<tr>");
                out.println("<td>" + pr.getIdProducto() + "</td>");
                out.println("<td>" + pr.getNombre() + "</td>");
                out.println("<td>" + pr.getCategoria() + "</td>");
                if (usernameOptional.isPresent()) {
                    out.println("<td>" + pr.getPrecio() + "</td>");
                    out.println("<td><form action='carrito' method='POST'>");
                    out.println("<input type='hidden' name='accion' value='agregar'>");
                    out.println("<input type='hidden' name='idProducto' value='" + pr.getIdProducto() + "'>");
                    out.println("<input type='hidden' name='nombre' value='" + pr.getNombre() + "'>");
                    out.println("<input type='hidden' name='categoria' value='" + pr.getCategoria() + "'>");
                    out.println("<input type='hidden' name='precio' value='" + pr.getPrecio() + "'>");
                    out.println("<button type='submit'>Agregar al carrito</button>");
                    out.println("</form></td>");
                }
                out.println("</tr>");
            });

            // Mostrar el carrito debajo de la tabla de productos
            if (carrito != null && !carrito.isEmpty()) {
                out.println("<h3>Carrito de compras:</h3>");
                out.println("<ul>");
                out.println("<br>");
                carrito.forEach(producto -> {
                    out.println("<br>");
                    out.println("<br><li>" + producto.getNombre() + " - $" + producto.getPrecio() +" x "+producto.getCantidad()+ "</li><br>");
                    out.println("<br>");
                });
                out.println("<br>");
                out.println("</ul>");

                double totalConIva = new CarritoServlet().calcularTotalConIva(req);
                out.println("<p>Total con IVA: $" + totalConIva + "</p>");
            }

            out.println("<a href='index.html'>Volver al Inicio</a>");
            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
