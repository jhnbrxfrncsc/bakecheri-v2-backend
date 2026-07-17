package dao.impl;

import config.DatabaseConfig;
import dao.ProductDAO;
import entity.Product;
import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

    private static final String SELECT_PRODUCT_COLUMNS = """
    SELECT
        id,
        name,
        description,
        category,
        price,
        stock_quantity,
        image_url,
        is_popular,
        created_at,
        updated_at
    FROM products
    """;

    private static final String FIND_BY_ID_SQL =
        SELECT_PRODUCT_COLUMNS + " WHERE id = ?;";

    private static final String FIND_POPULAR_SQL =
        SELECT_PRODUCT_COLUMNS + " WHERE is_popular IS TRUE;";

    private static final String FIND_BY_CATEGORY_SQL =
        SELECT_PRODUCT_COLUMNS + " WHERE LOWER(category) = LOWER(?);";

    private static final String SEARCH_SQL =
        SELECT_PRODUCT_COLUMNS + """
            WHERE LOWER(name) LIKE LOWER(?) 
            OR LOWER(description) LIKE LOWER(?) 
            OR LOWER(category) LIKE LOWER(?);
            """;

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_PRODUCT_COLUMNS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                logger.debug("findAll() -- Mapping product {}", rs.getLong("id"));
                products.add(mapRow(rs));
            }
            logger.info("findAll() -- Retrieved {} products", products.size());
        } catch (SQLException se) {
            logger.error("ProductDAOImpl#findAll() -- Failed to retrieve products", se);
            throw new DatabaseException("Failed to retrieve products");
        }

        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {
        logger.info("ProductDAOImpl#findById({}) -- START", id);
        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL);
        ) {
            logger.info("ProductDAOImpl#findById({}) - setting {} id to ps.", id, id);
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    logger.debug("findById() -- Mapping product {}", rs.getLong("id"));
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException se) {
            logger.error("Failed to retrieve product with id {}", id, se);
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findPopular() {
        logger.info("ProductDAOImpl#findPopular() -- START");
        List<Product> popularProducts = new ArrayList<>();
        try(
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(FIND_POPULAR_SQL);
            ResultSet rs = ps.executeQuery()
        ) {
            while(rs.next()) {
                logger.debug("findPopular() -- Mapping product {}", rs.getLong("id"));
                popularProducts.add(mapRow(rs));
            }
            logger.info("findPopular() -- Retrieved {} products", popularProducts.size());
        } catch(SQLException se) {
            logger.error("Failed to retrieve popular product/s", se);
        }

        logger.info("ProductDAOImpl#findPopular() -- END");
        return popularProducts;
    }

    @Override
    public List<Product> findByCategory(String category) {
        logger.info("ProductDAOImpl#findByCategory({}) -- START", category);
        List<Product> products = new ArrayList<>();

        try(
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(FIND_BY_CATEGORY_SQL);
                ) {
            ps.setString(1, category);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) {
                    logger.debug("findByCategory() -- Mapping product {}", rs.getLong("id"));
                    products.add(mapRow(rs));
                }
                logger.info("findByCategory() -- Retrieved {} products", products.size());
            } catch(SQLException se){
                logger.error("ProductDAOImpl#findByCategory() -- query execution failed.", se);
            }
        } catch(SQLException se) {
            logger.error("ProductDAOImpl#findByCategory() -- failed to retrieve products with {} category.", category, se);
        }

        logger.info("ProductDAOImpl#findByCategory() -- END");
        return products;
    }

    @Override
    public List<Product> search(String keyword) {
        logger.info("ProductDAOImpl#search({}) -- START", keyword);
        List<Product> searchedProducts = new ArrayList<>();

        try(
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(SEARCH_SQL);
                ){
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) {
                    logger.debug("search() - Mapping product {}", rs.getLong("id"));
                    searchedProducts.add(mapRow(rs));
                }
                logger.info("search() - Retrieved {} products", searchedProducts.size());
            } catch(SQLException se){
                logger.error("ProductDAOImpl#search({}) -- query execution failed.", keyword, se);
            }

        } catch (SQLException se) {
            logger.error("ProductDAOImpl#search() -- Failed to fetch products name/description/category with {}.", keyword, se);
        }

        return searchedProducts;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category"),
                rs.getBigDecimal("price"),
                rs.getInt("stock_quantity"),
                rs.getString("image_url"),
                rs.getBoolean("is_popular"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}
