package service;

import dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Retrieves all available products.
     *
     * @return never {@code null}; returns an empty list if no products exist.
     */
    List<ProductDTO> findAll();

    /**
     * get single product by id
     *
     * @param id id
     * @return {@link Optional}
     * @see Optional
     * @see ProductDTO
     */
    Optional<ProductDTO> findById(Long id);


    /**
     * Retrieves the popular products.
     *
     * @return popular products, or an empty list if none exist
     */
    List<ProductDTO> findPopular();

    /**
     Retrieves products belonging to the given category.
     *
     * @param category product category
     * @return matching products, or an empty list if none exist
     */
    List<ProductDTO> findByCategory(String category);

    /**
     * Retrieves all products (name/description/category) by keyword
     *
     * @param keyword keyword
     * @return matching products, or an empty list if none exist
     */
    List<ProductDTO> search(String keyword);
}
