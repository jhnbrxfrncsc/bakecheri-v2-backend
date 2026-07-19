package validation;

import dto.request.CreateProductRequest;
import dto.request.UpdateProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ProductValidator {

    private static final Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.+");

    private ProductValidator() {}

    public static void validate(CreateProductRequest reqProduct) {
        logger.info("Validating CreateProduct: {}", reqProduct.getName());

        validateName(reqProduct.getName());

        validateDescription(reqProduct.getDescription());

        validateCategory(reqProduct.getCategory());

        validatePrice(reqProduct.getPrice());

        validateStockQuantity(reqProduct.getStockQuantity());

        validateImageUrl(reqProduct.getImageUrl());

        logger.info("Done validating CreateProduct: {}", reqProduct.getName());
    }

    public static void validate(UpdateProductRequest reqProduct) {
        logger.info("Validating UpdateProduct: {}", reqProduct.getName());

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

        logger.info("Done validating UpdateProduct: {}", reqProduct.getName());
    }

    public static void validateName(String productName){
        logger.info("Validating product name: {}", productName);
        ValidationUtils.requireNotBlank(productName, "Name");
        ValidationUtils.requireLengthBetween(productName, "Name", 3, 30);
    }

    public static void validateDescription(String description){
        logger.debug("Validating product description: {}", description);
        ValidationUtils.requireNotBlank(description, "Description");
        ValidationUtils.requireLengthBetween(description, "Description", 10, 100);
    }

    public static void validatePrice(BigDecimal price){
        logger.debug("Validating product price: {}", price);
        ValidationUtils.requirePositive(price, "Price");
    }

    public static void validateCategory(String category){
        logger.debug("Validating product category: {}", category);
        ValidationUtils.requireNotBlank(category, "Category");
        ValidationUtils.requireMaxLength(category, "Category", 20);
    }

    public static void validateImageUrl(String imageUrl){
        logger.debug("Validating product image url: {}", imageUrl);
        ValidationUtils.requireNotBlank(imageUrl, "Image URL");
        ValidationUtils.requireMaxLength(imageUrl, "Image URL", 255);
        ValidationUtils.requirePattern(
                imageUrl,
                "Image URL",
                URL_PATTERN,
                "Image URL must start with 'http://' or 'https://'");
    }

    public static void validateStockQuantity(int stockQuantity){
        logger.debug("Validating product stock quantity: {}", stockQuantity);
        ValidationUtils.requireNonNegative(stockQuantity, "Stock quantity");
    }
}
