package mapper;

import dto.ProductDTO;
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
     * @return {@link ProductDTO}
     * @see ProductDTO
     */
    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
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
     * @see ProductDTO
     */
    public static List<ProductDTO> toDTOs(List<Product> products) {
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
    public static Product toEntity(ProductDTO dto) {
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
}
