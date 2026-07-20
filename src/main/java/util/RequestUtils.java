package util;

import exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import validation.ValidationUtils;

import java.io.IOException;

public final class RequestUtils {

    private RequestUtils() {
        throw new AssertionError("Utility class");
    }

    /*
     * ==========================
     * Query Parameters
     * ==========================
     */

    public static String getRequiredParameter(
            HttpServletRequest request,
            String parameterName
    ) {

        String value = request.getParameter(parameterName);

        if (!ValidationUtils.hasValue(value)) {
            throw new BadRequestException(parameterName + " is required.");
        }

        return value.trim();
    }

    public static String getOptionalParameter(
            HttpServletRequest request,
            String parameterName
    ) {

        String value = request.getParameter(parameterName);

        return ValidationUtils.hasValue(value)
                ? value.trim()
                : null;
    }

    public static Long getRequiredLongParameter(
            HttpServletRequest request,
            String parameterName
    ) {

        String value = getRequiredParameter(request, parameterName);

        try {

            return Long.parseLong(value);

        } catch (NumberFormatException ex) {

            throw new BadRequestException(
                    parameterName + " must be a valid number."
            );

        }

    }

    public static Long getOptionalLongParameter(
            HttpServletRequest request,
            String parameterName
    ) {

        String value = getOptionalParameter(request, parameterName);

        if (value == null) {
            return null;
        }

        try {

            return Long.parseLong(value);

        } catch (NumberFormatException ex) {

            throw new BadRequestException(
                    parameterName + " must be a valid number."
            );

        }

    }

    public static <T> T readBody(
            HttpServletRequest request,
            Class<T> clazz) {
        try {
            return JsonUtil
                    .mapper()
                    .readValue(request.getReader(), clazz);
        } catch(IOException e) {
            throw new BadRequestException("Invalid JSON request body.");
        }
    }
}
