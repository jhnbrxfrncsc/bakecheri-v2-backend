package service.impl;

import dao.ProductDAO;
import dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import mapper.ProductMapper;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<ProductDTO> findAll() {
        logger.info("Finding all products.");
        return ProductMapper.toDTOs(productDAO.findAll());
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        logger.info("Finding product with id: {}", id);
        return productDAO.findById(id)
                .map(ProductMapper::toDTO);
    }

    @Override
    public List<ProductDTO> findPopular() {
        logger.info("Finding popular products.");
        return ProductMapper.toDTOs(productDAO.findPopular());
    }

    @Override
    public List<ProductDTO> findByCategory(String category) {
        logger.info("Finding {} products", category);
        return ProductMapper.toDTOs(productDAO.findByCategory(category));
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        logger.info("Finding {} product name/description/category", keyword);
        return ProductMapper.toDTOs(productDAO.search(keyword));
    }
}
