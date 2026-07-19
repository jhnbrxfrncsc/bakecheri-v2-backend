package servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.impl.ProductDAOImpl;
import dto.request.CreateProductRequest;
import dto.request.UpdateProductRequest;
import dto.response.ProductResponse;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.HttpResponseUtil;
import util.JsonUtil;
import validation.ValidationUtils;

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
        if( ValidationUtils.hasValue(idParam) ){
            handleFindById(response, idParam);
            return;
        }

        // Case 2: Get popular products
        if( ValidationUtils.hasValue(popularParam) ) {
            if ( !popularParam.equalsIgnoreCase("true") ) {
                HttpResponseUtil.badRequest(response, "Invalid popular parameter" );
                return;
            }

            handleFindPopular(response);
            return;
        }

        // Case 3: Get products by Category
        if( ValidationUtils.hasValue(categoryParam) ){
            handleFindByCategory(response, categoryParam);
            return;
        }

        // Case 4: Search products (name/description/category)
        if( ValidationUtils.hasValue(keywordParam) ){
            handleSearch(response, keywordParam);
            return;

        }

        // Case 5: Get all products
        handleFindAll(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleCreate(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleUpdate(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleDelete(request, response);
    }

    private void handleFindById(HttpServletResponse response, String idParam) throws IOException {
        logger.info("ProductServlet#handleFindById -- START");
        try {
            long productId = Long.parseLong(idParam);
            logger.info("Finding product with id of '{}'", productId);

            ProductResponse product = productService.findById(productId);
            if (product != null) {
                logger.info("Found product: {}", product);
                HttpResponseUtil.ok(response,product, "Successfully fetched product with '" + product.getId() + "' id");
            } else {
                logger.info("Product not found");
                HttpResponseUtil.notFound(response, "Product not found");
            }
        } catch (NumberFormatException e) {
            logger.info("ProductServlet#doGet -- NumberFormatException: {}", e.getMessage());
            HttpResponseUtil.badRequest(response, "Invalid product id");
        }
        logger.info("ProductServlet#handleFindById -- END");
    }

    private void handleFindPopular(HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#handleFindPopular -- START");

        List<ProductResponse> popularProducts = productService.findPopular();
        HttpResponseUtil.ok(response,popularProducts, "Retrieved popular products was successfully fetched.");

        logger.info("ProductServlet#handleFindPopular -- END");
    }

    private void handleFindByCategory(HttpServletResponse response, String categoryParam) throws IOException {
        logger.info("ProductServlet#handleFindByCategory -- START");
        String category = categoryParam.trim().toLowerCase();
        logger.info("Retrieving products for category '{}'", category);

        List<ProductResponse> categoryProducts = productService.findByCategory(category);
        String message = !categoryProducts.isEmpty()
                ? "Retrieving products for category '" + category + "' was successful."
                : "No existing records for products with '" + category + "'";
        HttpResponseUtil.ok(response,categoryProducts, message);

        logger.info("ProductServlet#handleFindByCategory -- END");
    }

    private void handleSearch(HttpServletResponse response, String keywordParam) throws IOException {
        logger.info("ProductServlet#handleSearch -- START");
        String keyword = keywordParam.trim().toLowerCase();
        logger.info("Searching products using keyword: '{}'", keyword);

        List<ProductResponse> searchedProducts = productService.search(keyword);
        String message = !searchedProducts.isEmpty()
                ? "Retrieving products using '" + keyword + "' was successful."
                : "No existing records for products with '" + keyword + "' keyword";
        HttpResponseUtil.ok(response, searchedProducts, message);

        logger.info("ProductServlet#handleSearch -- END");
    }

    private void handleFindAll(HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#handleFindAll -- START");

        List<ProductResponse> products = productService.findAll();
        HttpResponseUtil.ok(response,products, "Retrieving all products was successful.");

        logger.info("ProductServlet#handleFindAll -- END");
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#handleCreate -- START");
        CreateProductRequest newProductRequest;

        // Read JSON Body
        try {
            newProductRequest = JsonUtil.mapper()
                    .readValue(request.getReader(), CreateProductRequest.class);
        } catch (JsonProcessingException jpe) {
            HttpResponseUtil.badRequest(response, "Invalid JSON request body");
            return;
        }

        Long productId = productService.create(newProductRequest);

        // return ok response along with the ID.
        HttpResponseUtil.created(
                response,
                productId,
                "Successfully created product. Product ID: '" + productId + "'");
        logger.info("ProductServlet#handleCreate -- END");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#handleUpdate -- START");
        UpdateProductRequest prodRequest;

        try{
            prodRequest = JsonUtil.mapper()
                    .readValue(request.getReader(), UpdateProductRequest.class);
        } catch(JsonProcessingException jpe) {
            HttpResponseUtil.badRequest(response, "Invalid JSON request body");
            return;
        }

        ProductResponse updatedProduct = productService.update(prodRequest);

        HttpResponseUtil.ok(
                response,
                updatedProduct,
                "Successfully updated the '" + updatedProduct.getName() + "' product.");

        logger.info("ProductServlet#handleUpdate -- END");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#handleDelete -- START");
        String idParam = request.getParameter("id");

        if (!ValidationUtils.hasValue(idParam)) {
            HttpResponseUtil.badRequest(response, "Product id is required");
            return;
        }

        Long productId;
        try {
            productId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            HttpResponseUtil.badRequest(response, "Invalid product id");
            return;
        }

        productService.delete(productId);

        HttpResponseUtil.ok(
                response,
                null,
                "Successfully deleted product with id: '" + productId + "'"
        );

        logger.info("ProductServlet#handleDelete -- END");
    }

}
