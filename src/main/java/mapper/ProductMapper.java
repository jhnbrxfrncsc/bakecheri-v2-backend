package mapper;

import dto.request.CreateProductRequest;
import dto.response.ProductResponse;
import entity.Product;

import java.util.List;
import java.util.Objects;

/**
 *  product mapper
 *
 */
public final class ProductMapper {

    private ProductMapper() {};

    /**
     * convert entity to dto
     *
     * @param product product
     * @return {@link ProductResponse}
     * @see ProductResponse
     */
    public static ProductResponse toDTO(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getImageUrl(),
                product.isPopular()
        );
    }

    /**
     * Convert entity list to DTO list
     *
     * @param products products
     * @return {@link List}
     * @see List
     * @see ProductResponse
     */
    public static List<ProductResponse> toDTOs(List<Product> products) {
        Objects.requireNonNull(products, "products must not be null");

        return products.stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    /**
     * convert DTO to entity
     *
     * @param dto dto
     * @return {@link Product}
     * @see Product
     */
    public static Product toEntity(ProductResponse dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getCategory(),
                dto.getPrice(),
                0, // stockQuantity (placeholder for now)
                dto.getImageUrl(),
                dto.isPopular(),
                null, // createdAt (placeholder for now)
                null // updatedAt (placeholder for now)
        );
    }

    /**
     * convert new product object to entity
     *
     * @param request CreateProductRequest
     * @return entity object
     */
    public static Product toEntity(CreateProductRequest request) {
        return new Product(
                null, // new record = null id
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getImageUrl(),
                false, // new records are non-popular products.
                null, // created_at column has already a default timestamp value
                null // updated_at column has already a default timestamp value
        );
    }
}
