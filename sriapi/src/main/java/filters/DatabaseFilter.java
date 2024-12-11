package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;

import utils.DatabaseConnection;

public class DatabaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Configuración inicial, si es necesaria
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            httpRequest.setAttribute("connection", connection);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException("Error establishing database connection", e);
        }
    }

    @Override
    public void destroy() {
        // Limpieza de recursos, si es necesaria
    }
}
