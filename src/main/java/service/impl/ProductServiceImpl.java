package service.impl;

import dao.ProductDAO;
import dto.request.UpdateProductRequest;
import dto.response.ProductResponse;
import dto.request.CreateProductRequest;
import entity.Product;
import exception.DatabaseException;
import exception.ResourceNotFoundException;
import exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import mapper.ProductMapper;
import util.UpdateUtil;
import validation.ProductValidator;
import validation.ValidationUtils;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<ProductResponse> findAll() {
        logger.info("Finding all products.");
        return ProductMapper.toDTOs(productDAO.findAll());
    }

    @Override
    public ProductResponse findById(Long id) {
        logger.info("Finding product with id: {}", id);
        Product product = getSingleProduct(id);
        return ProductMapper.toDTO(product);
    }

    @Override
    public List<ProductResponse> findPopular() {
        logger.info("Finding popular products.");
        return ProductMapper.toDTOs(productDAO.findPopular());
    }

    @Override
    public List<ProductResponse> findByCategory(String category) {
        logger.info("Finding {} products", category);
        return ProductMapper.toDTOs(productDAO.findByCategory(category));
    }

    @Override
    public List<ProductResponse> search(String keyword) {
        logger.info("Finding {} product name/description/category", keyword);
        return ProductMapper.toDTOs(productDAO.search(keyword));
    }

    @Override
    public Long create(CreateProductRequest reqProduct){
        logger.info("Creating product: {}", reqProduct);
        // Product validation
        ProductValidator.validate(reqProduct);

        // Throw an exception if product name is already existing.
        validateProductNameIsUnique(reqProduct.getName());

        // Convert to Entity
        Product product = ProductMapper.toEntity(reqProduct);

        // Persist
        return productDAO.create(product);
    }


    @Override
    public ProductResponse update(UpdateProductRequest reqProduct) {
        logger.info("Updating product....");

        // Retrieve existing product record
        Product existingProduct = getSingleProduct(reqProduct.getId());
        boolean hasChanges = false;

        logger.info("Updating product: {}", existingProduct.getName());
        // validate request object
        ProductValidator.validate(reqProduct);

        // perform checking of updated values before replacing existing field values.
        if (ValidationUtils.hasValue(reqProduct.getName())
                && !reqProduct.getName().equals(existingProduct.getName())) {

            // throw exception if updated product name is not unique
            validateProductNameIsUnique(existingProduct.getId(), reqProduct.getName());
            existingProduct.setName(reqProduct.getName());
            hasChanges = true;
        }

        hasChanges |= UpdateUtil.updateIfChanged(
                reqProduct.getDescription(), existingProduct.getDescription(), existingProduct::setDescription, "Description" );
        hasChanges |= UpdateUtil.updateIfChanged(
                reqProduct.getCategory(), existingProduct.getCategory(), existingProduct::setCategory, "Category" );
        hasChanges |= UpdateUtil.updateIfChanged(
                reqProduct.getPrice(), existingProduct.getPrice(), existingProduct::setPrice, "Price" );
        hasChanges |= UpdateUtil.updateIfChanged(
                reqProduct.getStockQuantity(), existingProduct.getStockQuantity(), existingProduct::setStockQuantity, "Stock Quantity" );
        hasChanges |= UpdateUtil.updateIfChanged(
                reqProduct.getImageUrl(), existingProduct.getImageUrl(), existingProduct::setImageUrl, "Image URL" );
        hasChanges |= UpdateUtil.updateIfChanged(
                reqProduct.getPopular(), existingProduct.isPopular(), existingProduct::setPopular, "Popular" );

        // if there are no updates, convert and return the existing product object.
        if( !hasChanges ) {
            logger.info("No changes for '{}' product", existingProduct.getName());
            return ProductMapper.toDTO(existingProduct);
        }

        // persist
        Product updatedProduct = productDAO.update(existingProduct)
                .orElseThrow(() -> new DatabaseException("Issue encountered during product update"));

        // convert entity to DTO & return
        return ProductMapper.toDTO(updatedProduct);
    }

    @Override
    public void delete(Long productId) {
        logger.info("Deleting product with id: {}", productId);

        if( !productDAO.delete(productId) ) {
            logger.warn("Product '{}' not found", productId);
            throw new ResourceNotFoundException("Product with id '" + productId + "' not found");
        }
    }

    /* PRIVATE METHODS */
    private Product getSingleProduct(Long productId){
        return productDAO
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private void validateProductNameIsUnique(String productName) {
        if (productDAO.existsByName(productName)) {
            throw new ValidationException("Product name already exists.");
        }
    }

    private void validateProductNameIsUnique(Long productId, String productName) {
        if (productDAO.existsByNameExcludingId(productName, productId)) {
            throw new ValidationException("Product name already exists.");
        }
    }
}
