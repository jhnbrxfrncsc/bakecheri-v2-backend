package util;

import dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class HttpResponseUtil {

    private HttpResponseUtil() {
    }

    public static void ok(HttpServletResponse response, Object data, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        JsonUtil.mapper().writeValue(response.getWriter(), new ApiResponse<>(true, data, message));
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
