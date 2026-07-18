package validation;

import dto.request.CreateProductRequest;
import dto.request.UpdateProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public final class ProductValidator {

    private static final Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    private ProductValidator() {}

    public static void validate(CreateProductRequest reqProduct) {
        logger.info("Validating CreateProduct: {}", reqProduct);

        validateName(reqProduct.getName());

        validateDescription(reqProduct.getDescription());

        validateCategory(reqProduct.getCategory());

        validatePrice(reqProduct.getPrice());

        validateStockQuantity(reqProduct.getStockQuantity());

        validateImageUrl(reqProduct.getImageUrl());

        logger.info("Done validating CreateProduct: {}", reqProduct);
    }

    public static void validate(UpdateProductRequest reqProduct) {
        logger.info("Validating UpdateProduct: {}", reqProduct);

        if (reqProduct.getName() != null) {
            validateName(reqProduct.getName());
        }

        if (reqProduct.getDescription() != null) {
            validateDescription(reqProduct.getDescription());
        }

        if (reqProduct.getCategory() != null) {
            validateCategory(reqProduct.getCategory());
        }

        if (reqProduct.getPrice() != null) {
            validatePrice(reqProduct.getPrice());
        }

        if (reqProduct.getStockQuantity() != null) {
            validateStockQuantity(reqProduct.getStockQuantity());
        }

        if (reqProduct.getImageUrl() != null) {
            validateImageUrl(reqProduct.getImageUrl());
        }

        logger.info("Done validating UpdateProduct: {}", reqProduct);
    }

    public static void validateName(String productName){
        logger.info("Validating product name: {}", productName);
        // validate product name param
        ValidationUtils.requireNotBlank(productName, "product 'name'");
    }

    public static void validateDescription(String description){
        logger.info("Validating product description: {}", description);

    }

    public static void validatePrice(BigDecimal price){
        logger.info("Validating product price: {}", price);

    }

    public static void validateCategory(String category){
        logger.info("Validating product category: {}", category);

    }

    public static void validateImageUrl(String imageUrl){
        logger.info("Validating product image url: {}", imageUrl);

    }

    public static void validateStockQuantity(int stockQuantity){
        logger.info("Validating product stock quantity: {}", stockQuantity);

    }
}
