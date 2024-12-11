package dao;

import models.VistaConsultaDetalle;
import java.sql.Connection;
import java.util.List;

public interface VistaConsultaDetalleDAO {
    List<VistaConsultaDetalle> getAll(Connection connection) throws Exception;
}
