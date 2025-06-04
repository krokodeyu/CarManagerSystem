package database.cms.service;

import database.cms.detail.CustomUserDetails;
import database.cms.repository.VehicleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final VehicleRepository vehicleRepository;

    public SecurityService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * 判断当前用户是否是资源的拥有者
     * @param authentication 当前认证对象
     * @param userId 请求中的目标用户ID
     * @return 如果是本人，返回 true；否则返回 false
     */
    public boolean isOwner(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getId().equals(userId);
        }

        return false;
    }

    public boolean isVehicleOwner(Authentication authentication, Long vehicleId) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return vehicleRepository.existsByIdAndUserId(vehicleId, userId);
    }
}