package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Productos;
import service.LoginService;
import service.LoginServiceSessionImplement;
import service.ProductoService;
import service.ProductoServiceImplement;

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


        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional=auth.getUsername(req);

        resp.setContentType("text/html;charset=UTF-8");
        try( PrintWriter out = resp.getWriter()) {
            //Creo la plantilla html
            out.print("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"utf-8\">");
            out.println("<title>Hola Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Listado de productos</h1>");
            if (usernameOptional.isPresent()){
                out.println("<div style='color:blue;'> Hola "+ usernameOptional.get() +" Bienvenido</div>");
            }
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID PRODUCTO</th>");
            out.println("<th>NOMBRE</th>");
            out.println("<th>CATEGORIA</th>");
            if (usernameOptional.isPresent()){
                out.println("<th>PRECIO</th>");
                out.println("<th>AGREGAR</th>");
            }

            out.println("</tr>");

            productos.forEach(pr ->{
                out.println("<tr>");
                out.println("<td>"+ pr.getIdProducto()+"</td>");
                out.println("<td>"+ pr.getNombre()+"</td>");
                out.println("<td>"+ pr.getCategoria()+"</td>");
                if(usernameOptional.isPresent()){
                    out.println("<td>"+ pr.getPrecio()+"</td>");
                    out.println("<td><a href=\""
                            + req.getContextPath()
                            +"/agregar-carro?idProducto="+ pr.getIdProducto()
                            +"\">Agregar carro</a></td>");
                }

                out.println("</tr>");
            } );
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
