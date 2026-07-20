package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException
    {
        HttpServletRequest req =
                (HttpServletRequest) request;

        long start = System.currentTimeMillis();

        logger.info("Incoming {} {}",
                req.getMethod(),
                req.getRequestURI());

        chain.doFilter(request, response);

        long end = System.currentTimeMillis();

        logger.info("Completed in {} ms",
                end - start);
    }
}
