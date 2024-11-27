package services;

import models.Productos;

import java.util.Arrays;
import java.util.List;

public class ProductoServiceImplement implements ProductoService{
    @Override
    public List<Productos> listar() {
        return Arrays.asList(new Productos(1,"Laptop", "tecnología",523.25),
                new Productos(2,"cocina", "hogar", 325.24),
                new Productos(3,"mouse","tecnología",15.25));

    }

}
