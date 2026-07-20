package util;

import dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class HttpResponseUtil {

    private HttpResponseUtil() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    private static void write(
            HttpServletResponse response,
            int status,
            Object data,
            String message
    ) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        boolean success = status >= 200 && status < 300;

        JsonUtil.mapper().writeValue(
                response.getWriter(),
                new ApiResponse<>(success, data, message)
        );

    }

    public static void ok(
            HttpServletResponse response,
            Object data,
            String message)
            throws IOException
    {
        write(response, HttpServletResponse.SC_OK, data, message);
    }

    public static void created(
            HttpServletResponse response,
            Object data, String message)
            throws IOException
    {
        write(response, HttpServletResponse.SC_CREATED, data, message);
    }

    public static void badRequest(
            HttpServletResponse response,
            String message) throws IOException
    {
        write(response, HttpServletResponse.SC_BAD_REQUEST, null, message);
    }

    public static void notFound(
            HttpServletResponse response,
            String message)
            throws IOException
    {
        write(response, HttpServletResponse.SC_NOT_FOUND, null, message);
    }

    public static void conflict(
            HttpServletResponse response,
            String message)
            throws IOException
    {
        write(response,
                HttpServletResponse.SC_CONFLICT,
                null,
                message);
    }

    public static void internalServerError(
            HttpServletResponse response,
            String message)
            throws IOException
    {
        write(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null, message);
    }
}
