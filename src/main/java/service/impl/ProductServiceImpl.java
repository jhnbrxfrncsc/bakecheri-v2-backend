package service.impl;

import dao.ProductDAO;
import entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> findAll() {
        logger.info("ProductServiceImpl#findAll - start");
        return productDAO.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        logger.info("ProductServiceImpl#findById({}) - start", id);
        return productDAO.findById(id);
    }
}
