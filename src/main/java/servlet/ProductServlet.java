package servlet;

import dao.impl.ProductDAOImpl;
import dto.response.ProductResponse;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.HttpResponseUtil;

import java.io.IOException;
import java.util.List;

public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);

    private final ProductService productService = new ProductServiceImpl( new ProductDAOImpl() );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#doGet -- START");

        final String idParam = request.getParameter("id");
        final String popularParam = request.getParameter("popular");
        final String categoryParam = request.getParameter("category");
        final String keywordParam = request.getParameter("search");


        // Established priority: id -> popular -> category -> search -> findAll
        // Case 1: Get single product by ID
        if( hasValue(idParam) ){
            handleFindById(response, idParam);
            return;
        }

        // Case 2: Get popular products
        if( hasValue(popularParam) ) {
            if ( !popularParam.equalsIgnoreCase("true") ) {
                HttpResponseUtil.writeError(
                        response, HttpServletResponse.SC_BAD_REQUEST, "Invalid popular parameter" );
                return;
            }

            handleFindPopular(response);
            return;
        }

        // Case 3: Get products by Category
        if( hasValue(categoryParam) ){
            handleFindByCategory(response, categoryParam);
            return;
        }

        // Case 4: Search products (name/description/category)
        if( hasValue(keywordParam) ){
            handleSearch(response, keywordParam);
            return;

        }

        // Case 5: Get all products
        handleFindAll(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    private void handleFindById(HttpServletResponse response, String idParam) throws IOException {
        try {
            long productId = Long.parseLong(idParam);
            logger.info("Finding product with id of '{}'", productId);

            ProductResponse product = productService
                    .findById(productId)
                    .orElse(null);
            if (product != null) {
                logger.info("Found product: {}", product);
                HttpResponseUtil.ok(response,product, "Successfully fetched product with '" + product.getId() + "' id");
            } else {
                logger.info("Product not found");
                HttpResponseUtil.writeError(response, HttpServletResponse.SC_NOT_FOUND, "Product not found");
            }
        } catch (NumberFormatException e) {
            logger.info("ProductServlet#doGet -- NumberFormatException: {}", e.getMessage());
            HttpResponseUtil.writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid product id");
        }
    }

    private void handleFindPopular(HttpServletResponse response) throws IOException {
        logger.info("Retrieving popular products....");

        List<ProductResponse> popularProducts = productService.findPopular();
        HttpResponseUtil.ok(response,popularProducts, "Retrieved popular products was successfully fetched.");

        logger.info("Popular products sent");
    }

    private void handleFindByCategory(HttpServletResponse response, String categoryParam) throws IOException {
        String category = categoryParam.trim().toLowerCase();
        logger.info("Retrieving products for category '{}'", category);

        List<ProductResponse> categoryProducts = productService.findByCategory(category);
        String message = !categoryProducts.isEmpty()
                ? "Retrieving products for category '" + category + "' was successful."
                : "No existing records for products with '" + category + "'";
        HttpResponseUtil.ok(response,categoryProducts, message);

        logger.info("Categories products sent");
    }

    private void handleSearch(HttpServletResponse response, String keywordParam) throws IOException {
        String keyword = keywordParam.trim().toLowerCase();
        logger.info("Searching products using keyword: '{}'", keyword);

        List<ProductResponse> searchedProducts = productService.search(keyword);
        String message = !searchedProducts.isEmpty()
                ? "Retrieving products using '" + keyword + "' was successful."
                : "No existing records for products with '" + keyword + "' keyword";
        HttpResponseUtil.ok(response, searchedProducts, message);

        logger.info("Searched products sent");
    }

    private void handleFindAll(HttpServletResponse response) throws IOException {
        logger.info("Fetching all products....");

        List<ProductResponse> products = productService.findAll();
        HttpResponseUtil.ok(response,products, "Retrieving all products was successful.");

        logger.info("All products sent");
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

}
