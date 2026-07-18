package service.impl;

import dao.ProductDAO;
import dto.request.UpdateProductRequest;
import dto.response.ProductResponse;
import dto.request.CreateProductRequest;
import entity.Product;
import exception.DatabaseException;
import exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import mapper.ProductMapper;
import util.UpdateUtil;
import validation.ProductValidator;

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
    public boolean existsByName(String name) {
        logger.info("Finding if product '{}' is existing", name);
        return productDAO.existsByName(name);
    }


    @Override
    public Long create(CreateProductRequest reqProduct){
        logger.info("Creating product: {}", reqProduct);
        // Product validation
        ProductValidator.validate(reqProduct);

        // Convert to Entity
        Product product = ProductMapper.toEntity(reqProduct);

        // Persist
        return productDAO.create(product);
    }


    @Override
    public ProductResponse update(UpdateProductRequest reqProduct) {
        logger.info("Updating product: {}", reqProduct);

        // Retrieve existing product record
        Product existingProduct = getSingleProduct(reqProduct.getId());
        boolean updated = false;

        // validate request object
        ProductValidator.validate(reqProduct);

        // perform checking of updated values before replacing existing field values.
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getName(), existingProduct.getName(), existingProduct::setName );
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getDescription(), existingProduct.getDescription(), existingProduct::setDescription );
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getCategory(), existingProduct.getCategory(), existingProduct::setCategory );
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getPrice(), existingProduct.getPrice(), existingProduct::setPrice );
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getStockQuantity(), existingProduct.getStockQuantity(), existingProduct::setStockQuantity );
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getImageUrl(), existingProduct.getImageUrl(), existingProduct::setImageUrl );
        updated |= UpdateUtil.updateIfChanged(
                reqProduct.getPopular(), existingProduct.isPopular(), existingProduct::setPopular );

        // if there are no updates, convert and return the existing product object.
        if( !updated ) {
            logger.info("No changes for '{}' product", existingProduct.getName());
            return ProductMapper.toDTO(existingProduct);
        }

        // persist
        Product updatedProduct = productDAO.update(existingProduct)
                .orElseThrow(() -> new DatabaseException("Issue encountered during product update"));

        // convert entity to DTO & return
        return ProductMapper.toDTO(updatedProduct);
    }


    /* PRIVATE METHODS */
    private Product getSingleProduct(Long productId){
        return productDAO
                .findById(productId)
                .orElseThrow(() -> new ValidationException("Product not found with id: " + productId));
    }
}
