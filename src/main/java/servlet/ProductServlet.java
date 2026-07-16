package servlet;

import dao.ProductDAO;
import dao.impl.ProductDAOImpl;
import entity.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.HttpResponseUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class.getName());
    
    private final ProductDAO productDAO = new ProductDAOImpl();
    private final ProductService productService = new ProductServiceImpl(productDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("ProductServlet#doGet -- START");

        String idParam = request.getParameter("id");

        // Case 1: Get all products
        if (idParam == null || idParam.isBlank()) {
            logger.info("ProductServlet#doGet -- Fetching all products");
            List<Product> products = productService.findAll();
            HttpResponseUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, products);
            logger.info("ProductServlet#doGet -- All products sent");
            return;
        }

        // Case 2: Get single product by ID
        try {
            long productId = Long.parseLong(idParam);
            logger.info("ProductServlet#doGet -- Find productId: {}", productId);

            Optional<Product> optProduct = productService.findById(productId);
            if (optProduct.isPresent()) {
                Product product = optProduct.get();
                logger.info("ProductServlet#doGet -- Found product: {}", product);
                HttpResponseUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, product);
            } else {
                logger.info("ProductServlet#doGet -- Product not found");
                HttpResponseUtil.writeError(response, HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
        } catch (NumberFormatException e) {
            logger.info("ProductServlet#doGet -- NumberFormatException: {}", e.getMessage());
            HttpResponseUtil.writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
            return;
        }
        logger.info("ProductServlet#doGet -- END");
    }

}
