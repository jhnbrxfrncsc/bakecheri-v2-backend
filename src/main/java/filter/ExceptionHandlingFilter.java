package filter;

import exception.BadRequestException;
import exception.DatabaseException;
import exception.ResourceNotFoundException;
import exception.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtil;

import java.io.IOException;

public class ExceptionHandlingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse responses,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res =
                (HttpServletResponse) responses;

        try {

            chain.doFilter(request, res);

        } catch (ValidationException ex) {

            logger.warn("Request validation failed: {}", ex.getMessage());

            HttpResponseUtil.badRequest(
                    res,
                    ex.getMessage());

        } catch (BadRequestException ex) {

            logger.warn(ex.getMessage());

            HttpResponseUtil.badRequest(
                    res,
                    ex.getMessage());

        } catch (ResourceNotFoundException ex) {

            logger.warn("Resource not found: {}", ex.getMessage());

            HttpResponseUtil.notFound(
                    res,
                    ex.getMessage());

        } catch (DatabaseException ex) {

            logger.error(ex.getMessage(), ex);

            HttpResponseUtil.internalServerError(
                    res,
                    "Internal server error");

        } catch (Exception ex) {

            logger.error("Unexpected error", ex);

            HttpResponseUtil.internalServerError(
                    res,
                    "Unexpected server error");
        }
    }
}
