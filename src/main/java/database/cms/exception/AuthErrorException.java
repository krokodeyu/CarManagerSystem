package database.cms.exception;

import lombok.Getter;

@Getter
public class AuthErrorException extends RuntimeException {
    private final String errorCode;

    public AuthErrorException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
