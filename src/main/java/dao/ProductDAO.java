package dao;

import entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {

    List<Product> findAll();
    Optional<Product> findById(Long id);
}
