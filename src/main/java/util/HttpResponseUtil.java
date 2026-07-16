package util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class HttpResponseUtil {

    private HttpResponseUtil() {
    }

    public static void writeJsonResponse(
            HttpServletResponse response,
            int statusCode,
            Object body) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        JsonUtil.mapper().writeValue(response.getWriter(), body);
    }

    public static void writeError(
            HttpServletResponse response,
            int statusCode,
            String message) throws IOException {
        String json = "{\"message\":\"" + message + "\"}";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        response.getWriter().write(json);
    }
}
