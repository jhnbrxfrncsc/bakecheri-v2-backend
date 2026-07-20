package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HttpResponseUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpResponseUtil.ok(
                response,
                Map.of(
                        "status", "UP",
                        "timestamp", LocalDateTime.now()
                ),
                "Application is running."
        );

    }

}