package database.cms.DTO.request;

public record AddVehicleRequest(
        String token,
        String model,
        String licensePlate
) {
}
