package dao.impl;

import config.DatabaseConfig;
import dao.ProductDAO;
import entity.Product;
import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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

    private static final String FIND_PRODUCT_NAME_SQL = """
           SELECT EXISTS ( 
               SELECT 1 
               FROM products 
               WHERE name = ?
           );
        """;

    private static final String FIND_PRODUCT_NAME_EXCLUDING_ID_SQL = """
           SELECT EXISTS ( 
               SELECT 1 
               FROM products 
               WHERE name = ?
               AND id <> ?
           );
        """;

    private static final String INSERT_SQL = """
            INSERT INTO products (name, description, category, price, stock_quantity, image_url)
            VALUES (?, ?, ?, ?, ?, ?);
        """;

    private static final String UPDATE_SQL = """
            UPDATE products
            SET
                name=?,
                description=?,
                category=?,
                price=?,
                stock_quantity=?,
                image_url=?,
                is_popular=?,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            RETURNING *;
        """;

    private static final String DELETE_SQL = "DELETE FROM products WHERE id = ?";

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
            throw new DatabaseException("Unable to retrieve products", se);
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
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    logger.debug("findById() -- Mapping product {}", rs.getLong("id"));
                    return Optional.of(mapRow(rs));
                }
            } catch (SQLException se) {
                logger.error("findById() -- query execution failed", se);
                throw new DatabaseException("Query execution failed.", se);
            }

        } catch (SQLException se) {
            logger.error("Failed to retrieve product with id {}", id, se);
            throw new DatabaseException("Unable to retrieve product with '" + id + "' id", se);
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
            logger.error("findPopular() -- Failed to retrieve popular product/s", se);
            throw new DatabaseException("Unable to retrieve popular product/s", se);
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
                logger.error("findByCategory() -- query execution failed.", se);
                throw new DatabaseException("Query execution failed", se);
            }
        } catch(SQLException se) {
            logger.error("ProductDAOImpl#findByCategory() -- failed to retrieve products with {} category.", category, se);
            throw new DatabaseException("Failed to retrieve products with category " + category, se);
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
                    logger.debug("search() -- Mapping product {}", rs.getLong("id"));
                    searchedProducts.add(mapRow(rs));
                }
                logger.info("search() -- Retrieved {} products", searchedProducts.size());
            } catch(SQLException se){
                logger.error("search() -- -- query execution failed.", se);
                throw new DatabaseException("Query execution failed", se);
            }

        } catch (SQLException se) {
            logger.error("search() -- Failed to fetch products name/description/category with {}.", keyword, se);
            throw new DatabaseException(
                    "Unable to retrieve product (name/description/category) using '" + keyword + "' keyword"
                    , se);
        }

        return searchedProducts;
    }

    @Override
    public boolean delete(Long productId){
        logger.info("Executing DELETE for product {}", productId);
        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_SQL)
        ) {
            ps.setLong(1, productId);

            int affectedRows = ps.executeUpdate();

            logger.info(
                    "Deleted {} row(s) for product id={}",
                    affectedRows,
                    productId
            );

            return affectedRows > 0;
        } catch(SQLException se) {
            logger.error("delete() -- Failed to delete product with '{}' id", productId, se);
            throw new DatabaseException("Failed to delete product with id " + productId, se);
        }
    }

    public boolean existsByName(String name){
        logger.info("ProductDAOImpl#existsByName({}) -- START", name);
        try(
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(FIND_PRODUCT_NAME_SQL);
        ) {
            ps.setString(1, name);

            return exists(ps);

        } catch (SQLException se) {
            logger.error("existsByName() -- Issue/s encountered in Database Connection.", se);
            throw new DatabaseException("Issue/s encountered in Database Connection", se);
        }

    }

    @Override
    public boolean existsByNameExcludingId(String name,  Long id){
        logger.info("ProductDAOImpl#existsByNameExcludingId({}, {}) -- START", name, id);
        try(
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(FIND_PRODUCT_NAME_EXCLUDING_ID_SQL);
        ) {
            ps.setString(1, name);
            ps.setLong(2, id);

            return exists(ps);

        } catch (SQLException se) {
            logger.error("existsByNameExcludingId() -- Issue/s encountered in Database Connection.", se);
            throw new DatabaseException("Issue/s encountered in Database Connection", se);
        }
    }

    public Long create(Product product) {
        logger.info("Saving product '{}'", product.getName());
        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQuantity());
            ps.setString(6, product.getImageUrl());

            int affectedRows = ps.executeUpdate();
            logger.info("affectedRows() -- {}", affectedRows);
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to create product '" + product.getName() + "'. No rows were inserted.");
            }

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    Long id = rs.getLong(1);
                    logger.info(
                            "Product '{}' saved with id={}",
                            product.getName(),
                            id
                    );
                    return id;
                }
            }

            throw new DatabaseException("Creating product failed. No generated ID returned");
        } catch(SQLException se) {
            logger.error("ProductDAOImpl#save() -- query execution failed. Unable to save product '{}'", product.getName(), se);
            throw new DatabaseException("Unable to save product", se);
        }
    }

    @Override
    public Optional<Product> update(Product product) {
        logger.info("ProductDAOImpl#update({}) -- START", product.getName());
        try(
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
        ) {
            ps.setString(1,  product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQuantity());
            ps.setString(6, product.getImageUrl());
            ps.setBoolean(7, product.isPopular());
            ps.setLong(8, product.getId());

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    logger.debug("update() -- Mapping product {}", rs.getLong(1));
                    return Optional.of(mapRow(rs));
                }
            } catch(SQLException se) {
                logger.error("update() -- Failed to fetch product '{}'", product.getName(), se);
                throw new DatabaseException("Unable to update product", se);
            }
        } catch(SQLException se) {
            logger.error("An error was occurred in database connection");
            throw new DatabaseException("An error was occurred in db connection", se);
        }
        return Optional.empty();
    }

    /* PRIVATE METHODS */
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

    private boolean exists(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getBoolean(1);
        }
    }

//    private String buildUpdateDynamicSQL(Product product) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("UPDATE products SET ");
//
//        return sql.toString();
//    }
}
