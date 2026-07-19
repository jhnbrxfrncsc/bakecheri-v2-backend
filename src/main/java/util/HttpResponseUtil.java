package util;

import dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class HttpResponseUtil {

    private HttpResponseUtil() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    public static void ok(HttpServletResponse response, Object data, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(true, data, message));
    }

    public static void created(HttpServletResponse response, Object data, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_CREATED);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(true, data, message));
    }

    public static void badRequest(
            HttpServletResponse response,
            String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(false, null, message));
    }

    public static void notFound(
            HttpServletResponse response,
            String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(false, null, message));
    }

    public static void internalServerError(
            HttpServletResponse response,
            String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(false, null, message));
    }

    public static void writeError(
            HttpServletResponse response,
            int statusCode,
            String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(false, null, message));
    }
}
