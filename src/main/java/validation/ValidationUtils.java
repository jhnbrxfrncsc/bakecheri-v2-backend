package validation;

import exception.ValidationException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    /*
       ===========================
       Object Validation
       ===========================
    */

    public static void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    /*
       ===========================
       String Validation
       ===========================
    */

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void requireMinLength(String value, String fieldName, int minLength) {
        requireNotBlank(value, fieldName);

        if (value.length() < minLength) {
            throw new ValidationException(
                    String.format("%s must be at least %d characters.", fieldName, minLength)
            );
        }
    }

    public static void requireMaxLength(String value, String fieldName, int maxLength) {
        requireNotBlank(value, fieldName);

        if (value.length() > maxLength) {
            throw new ValidationException(
                    String.format("%s must not exceed %d characters.", fieldName, maxLength)
            );
        }
    }

    public static void requireLengthBetween(
            String value,
            String fieldName,
            int minLength,
            int maxLength
    ) {
        requireNotBlank(value, fieldName);

        int length = value.length();

        if (length < minLength || length > maxLength) {
            throw new ValidationException(
                    String.format("%s must be between %d and %d characters.",
                            fieldName,
                            minLength,
                            maxLength)
            );
        }
    }

    public static void requirePattern(
            String value,
            String fieldName,
            Pattern pattern,
            String message
    ) {
        requireNotBlank(value, fieldName);

        if (!pattern.matcher(value).matches()) {
            throw new ValidationException(message);
        }
    }

    public static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean hasValue(Long value) {
        return value != null && value <= 0;
    }

    /*
       ===========================
       Number Validation
       ===========================
    */

    public static void requirePositive(BigDecimal value, String fieldName) {
        requireNotNull(value, fieldName);

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName + " must be greater than 0.");
        }
    }

    public static void requirePositive(Integer value, String fieldName) {
        requireNotNull(value, fieldName);

        if (value <= 0) {
            throw new ValidationException(fieldName + " must be greater than 0.");
        }
    }

    public static void requireNonNegative(Integer value, String fieldName) {
        requireNotNull(value, fieldName);

        if (value < 0) {
            throw new ValidationException(fieldName + " cannot be negative.");
        }
    }

    public static void requireBetween(
            BigDecimal value,
            String fieldName,
            BigDecimal min,
            BigDecimal max
    ) {
        requireNotNull(value, fieldName);

        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new ValidationException(
                    String.format("%s must be between %s and %s.",
                            fieldName,
                            min,
                            max)
            );
        }
    }

    /*
       ===========================
       Collection Validation
       ===========================
    */

    public static void requireNotEmpty(Collection<?> collection, String fieldName) {
        if (collection == null || collection.isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }
    }
}
