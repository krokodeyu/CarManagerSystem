package database.cms.DTO.request;

public record RepairItemsDeleteRequest(
        Long appointmentId,
        Long repairItemId
) {
}
