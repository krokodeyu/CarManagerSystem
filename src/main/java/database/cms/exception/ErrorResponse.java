package database.cms.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
