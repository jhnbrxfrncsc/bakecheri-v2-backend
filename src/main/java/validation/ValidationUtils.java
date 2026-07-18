package validation;

import exception.ValidationException;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void requireNotBlank(String value, String fieldName) {
        if(value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }
}
