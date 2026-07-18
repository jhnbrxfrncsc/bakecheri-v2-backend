package util;

import java.util.function.Consumer;

public class UpdateUtil {

    private UpdateUtil(){}

    public static <T> boolean updateIfChanged(
            T newValue,
            T currentValue,
            Consumer<T> setter
    ) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    public static boolean updateIfChanged(
            String newValue,
            String currentValue,
            Consumer<String> setter
    ) {
        if (newValue != null
                && !newValue.isBlank()
                && !newValue.equals(currentValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
