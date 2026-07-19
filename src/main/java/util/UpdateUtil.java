package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class UpdateUtil {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUtil.class);

    private UpdateUtil(){
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    public static <T> boolean updateIfChanged(
            T newValue,
            T currentValue,
            Consumer<T> setter,
            String fieldName
    ) {
        if (newValue != null && !newValue.equals(currentValue)) {
            logger.info(
                    "Detected change for field '{}'. Current value: '{}', New value: '{}'",
                    fieldName,
                    currentValue,
                    newValue
            );
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean updateIfChanged(
            String newValue,
            String currentValue,
            Consumer<String> setter,
            String fieldName
    ) {
        if (newValue != null
                && !newValue.isBlank()
                && !newValue.equals(currentValue))
        {
            logger.info(
                    "Updating field '{}': '{}' -> '{}'",
                    fieldName,
                    currentValue,
                    newValue
            );
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
