package database.cms.DTO.request;

public record RepairItemsAddRequest(
        Long appointmentId,
        String Description,
        double cost
) {
}
