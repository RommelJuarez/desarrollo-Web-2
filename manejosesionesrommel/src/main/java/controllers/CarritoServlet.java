package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Productos;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/carrito")
public class CarritoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");

        HttpSession session = req.getSession();
        List<Productos> carrito = (List<Productos>) session.getAttribute("carrito");

        // Si el carrito no existe, crearlo
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }

        if ("agregar".equals(accion)) {
            int idProducto = Integer.parseInt(req.getParameter("idProducto"));
            String nombre = req.getParameter("nombre");
            String categoria = req.getParameter("categoria");
            double precio = Double.parseDouble(req.getParameter("precio"));


            // Buscar si el producto ya está en el carrito
            Productos productoExistente = carrito.stream()
                    .filter(p -> p.getIdProducto() == idProducto)
                    .findFirst()
                    .orElse(null);

            if (productoExistente != null) {
                // Si el producto ya está en el carrito, se incrementa la cantidad
                productoExistente.setPrecio(productoExistente.getPrecio() + precio);
                productoExistente.setCantidad(productoExistente.getCantidad() + 1);
            } else {
                // Si no está en el carrito, se agrega como nuevo producto
                Productos producto = new Productos(idProducto, nombre, categoria, precio);
                producto.setCantidad(1);
                carrito.add(producto);
            }
        }

        // Redirigir al servlet de productos para actualizar la vista
        resp.sendRedirect(req.getContextPath() + "/productos");
    }

    public double calcularTotalConIva(HttpServletRequest req) {
        HttpSession session = req.getSession();
        List<Productos> carrito = (List<Productos>) session.getAttribute("carrito");

        if (carrito != null) {
            double total = carrito.stream()
                    .mapToDouble(Productos::getPrecio)
                    .sum();
            return total * 1.15; // Total con IVA (15%)
        }
        return 0;
    }
}
