package util;

import dto.request.UpdateProductRequest;
import exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import validation.ValidationUtils;

import java.io.IOException;

public class RequestUtil {

    private RequestUtil() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    public static Long getRequiredLongParameter(
            HttpServletRequest request,
            String parameterName
    ){

        String value = request.getParameter(parameterName);

        if( !ValidationUtils.hasValue(value)){
            throw new BadRequestException(
                    parameterName + " is required."
            );
        }

        try{
            return Long.parseLong(value);
        }catch(NumberFormatException ex){
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
