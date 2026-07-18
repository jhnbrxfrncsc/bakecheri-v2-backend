package exception;

import jakarta.servlet.http.HttpServletResponse;

public class DatabaseException extends ApiException {

    public DatabaseException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(HttpServletResponse.SC_NOT_FOUND, message, cause);
    }
}
