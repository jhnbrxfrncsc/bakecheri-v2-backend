package service;

import entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Retrieves all available products.
     *
     * @return never {@code null}; returns an empty list if no products exist.
     */
    List<Product> findAll();

    /**
     * get single product by id
     *
     * @param id id
     * @return {@link Optional}
     * @see Optional
     * @see Product
     */
    Optional<Product> findById(Long id);
}
