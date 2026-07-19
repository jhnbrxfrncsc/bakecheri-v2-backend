package dao;

import entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findPopular();

    List<Product> findByCategory(String category);

    List<Product> search(String keyword);

    boolean existsByName(String name);

    boolean existsByNameExcludingId(String name, Long id);

    Long create(Product product);

    Optional<Product> update(Product product);
}
