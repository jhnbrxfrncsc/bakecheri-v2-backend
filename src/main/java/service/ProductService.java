package service;

import dto.request.UpdateProductRequest;
import dto.response.ProductResponse;
import dto.request.CreateProductRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Retrieves all available products.
     *
     * @return never {@code null}; returns an empty list if no products exist.
     */
    List<ProductResponse> findAll();

    /**
     * get single product by id
     *
     * @param id id
     * @return {@link Optional}
     * @see Optional
     * @see ProductResponse
     */
    ProductResponse findById(Long id);


    /**
     * Retrieves the popular products.
     *
     * @return popular products, or an empty list if none exist
     */
    List<ProductResponse> findPopular();

    /**
     Retrieves products belonging to the given category.
     *
     * @param category product category
     * @return matching products, or an empty list if none exist
     */
    List<ProductResponse> findByCategory(String category);

    /**
     * Retrieves all products (name/description/category) by keyword
     *
     * @param keyword keyword
     * @return matching products, or an empty list if none exist
     */
    List<ProductResponse> search(String keyword);

    /**
     * New product creation.
     *
     * @param createProductRequest CreateProductRequest
     * @return created product's ID
     */
    Long create(CreateProductRequest createProductRequest);

    /**
     * Update existing product record.
     *
     * @param updateProductRequest updateProductRequest
     * @return ProductResponse
     */
    ProductResponse update(UpdateProductRequest updateProductRequest);

    /**
     * Delete a product record.
     *
     * @param id product id
     */
    void delete(Long id);

}
