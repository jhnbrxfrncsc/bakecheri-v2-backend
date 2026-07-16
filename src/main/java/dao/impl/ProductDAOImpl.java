package dao.impl;

import config.DatabaseConfig;
import dao.ProductDAO;
import entity.Product;
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

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_PRODUCT_COLUMNS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logger.error("Failed to retrieve products", e);
        }

        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {
        logger.info("ProductDAOImpl#findById({}) - start", id);
        String sql = SELECT_PRODUCT_COLUMNS + " WHERE id = ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            logger.info("ProductDAOImpl#findById({}) - setting {} id to ps.", id, id);
            ps.setLong(1, id);

            logger.info("ProductDAOImpl#findById({}) - executing query....", id);
            try(ResultSet rs = ps.executeQuery()){
            logger.info("ProductDAOImpl#findById({}) : rs = {}", id, rs.toString());
                if (rs.next()) {
                    logger.info("ProductDAOImpl#findById({}) - successful....", id);
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to retrieve product with id {}", id, e);
        }

        return Optional.empty();
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
