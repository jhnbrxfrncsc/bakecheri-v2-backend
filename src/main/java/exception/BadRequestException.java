package exception;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }}
