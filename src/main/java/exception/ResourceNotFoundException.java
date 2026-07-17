package exception;

import jakarta.servlet.http.HttpServletResponse;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }
}
