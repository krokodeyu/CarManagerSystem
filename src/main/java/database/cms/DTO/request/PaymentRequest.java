package database.cms.DTO.request;

public record PaymentRequest(
        Long technicianId,
        double amount
) {
}
