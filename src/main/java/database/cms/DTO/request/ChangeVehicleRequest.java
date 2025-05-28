package database.cms.DTO.request;

public record ChangeVehicleRequest(
        Long vehicleId,
        String model,
        String licensePlate
) {
}
