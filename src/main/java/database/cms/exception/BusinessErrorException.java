package database.cms.exception;

import lombok.Getter;

@Getter
public class BusinessErrorException extends RuntimeException {
    private final String errorCode;

    public BusinessErrorException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
