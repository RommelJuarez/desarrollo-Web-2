package dao.impl;

import dao.VistaConsultaDetalleDAO;
import models.VistaConsultaDetalle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VistaConsultaDetalleDAOImpl implements VistaConsultaDetalleDAO {

    @Override
    public List<VistaConsultaDetalle> getAll(Connection connection) throws Exception {
        // Definición de la consulta SQL
        String query = "SELECT * FROM vista_consulta_detalle";

        // Uso de try-with-resources para cerrar automáticamente recursos
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Lista para almacenar los resultados
            List<VistaConsultaDetalle> lista = new ArrayList<>();

            // Procesar cada fila del resultado
            while (resultSet.next()) {
                VistaConsultaDetalle detalle = new VistaConsultaDetalle(
                        resultSet.getString("numeroRuc"),
                        resultSet.getString("razonSocial"),
                        resultSet.getString("estadoContribuyenteRuc"),
                        resultSet.getString("actividadEconomicaPrincipal"),
                        resultSet.getString("tipoContribuyente"),
                        resultSet.getString("regimen"),
                        resultSet.getString("categoria")
                );
                lista.add(detalle);
            }

            // Devolver la lista
            return lista;
        }
    }

    // Método para obtener los detalles por RUC
    public List<VistaConsultaDetalle> getDetallesPorRuc(Connection connection, String ruc) throws Exception {
        // Definición de la consulta SQL filtrada por RUC
        String query = "SELECT * FROM vista_consulta_detalle WHERE numeroRuc = ?";

        // Uso de try-with-resources para cerrar automáticamente recursos
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ruc);  // Se establece el parámetro de RUC

            try (ResultSet resultSet = statement.executeQuery()) {
                // Lista para almacenar los resultados
                List<VistaConsultaDetalle> lista = new ArrayList<>();

                // Procesar cada fila del resultado
                while (resultSet.next()) {
                    VistaConsultaDetalle detalle = new VistaConsultaDetalle(
                            resultSet.getString("numeroRuc"),
                            resultSet.getString("razonSocial"),
                            resultSet.getString("estadoContribuyenteRuc"),
                            resultSet.getString("actividadEconomicaPrincipal"),
                            resultSet.getString("tipoContribuyente"),
                            resultSet.getString("regimen"),
                            resultSet.getString("categoria")
                    );
                    lista.add(detalle);
                }

                // Devolver la lista
                return lista;
            }
        }
    }
}
