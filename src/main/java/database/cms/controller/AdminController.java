package database.cms.controller;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.service.AdminService;
import database.cms.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/*管理员控制`/admin`

        | 方法     | 路径                   | 描述          | 权限    |
        | ------ | -------------------- | ----------- | ----- |
        | `GET`  | `/admin/users`       | 查询用户        | admin |
        | `GET`  | `/admin/technicians` | 查询维修人员      | admin |
        | `GET`  | `/admin/salaries`    | 查询所有工时费发放记录 | admin |
        | `GET`  | `/admin/vehicles`    | 查询车辆        | admin |
        | `GET`  | `/admin/orders`      | 查询特定维修工单信息  | admin |
        | `GET`  | `/admin/records`     | 查询所有历史维修记录  | admin |
        | `POST` | `/admin/salaries`    | 发放工时费       | admin |

*/
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;


    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserCheckResponse> checkUser(@PathVariable Long userId) {
        UserCheckResponse response = adminService.checkUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/technicians/{technicianId}")
    public ResponseEntity<TechnicianCheckResponse> checkTechnician(@PathVariable Long technicianId) {
        TechnicianCheckResponse response = adminService.checkTechnician(technicianId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/salaries")
    public ResponseEntity<SalaryRecordResponse> checkAllSalaries() {
        SalaryRecordResponse response = adminService.checkAllSalaryRecord();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleCheckResponse> checkVehicle(@PathVariable Long vehicleId) {
        VehicleCheckResponse response = adminService.checkVehicle(vehicleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderCheckResponse> checkOrder(@PathVariable Long orderId) {
        OrderCheckResponse response = adminService.checkOrder(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/records")
    public ResponseEntity<MaintenanceRecordResponse> checkAllRecords() {
        MaintenanceRecordResponse response = adminService.checkAllMaintenanceRecord();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> changePassword(
            Authentication auth,
            @RequestBody ChangePasswordRequest request
    ) {
        MessageResponse response = adminService.changePassword(auth, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pay-technician")
    public ResponseEntity<MessageResponse> payTechnician(@RequestBody PaymentRequest request) {
        adminService.payTechnician(request.technicianId(), request.amount());
        MessageResponse response = new MessageResponse("success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/average-repair-fees")
    public ResponseEntity<?> statsAverageRepairFees(String model) {
        AverageRepairFeeResponse response = adminService.statsAverageRepairFees(model);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/repair-frequencies")
    public ResponseEntity<?> statsRepairFrequencies() {
        RepairFrequenciesResponse response = adminService.statsFrequencies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/most-frequent-failures")
    public ResponseEntity<?> statsMostFrequentFailures(@RequestParam String model) {
        MostFrequentFailuresResponse response = adminService.statsMostFrequentFailures(model);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/fee-proportions")
    public ResponseEntity<?> statsFeeProportions(@RequestParam Integer year, Integer month) {
        FeeProportionResponse response = adminService.statsFeeProportions(year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/negative-comment-orders")
    public ResponseEntity<?> statsNegativeCommentOrders() {
        NegativeCommentOrdersResponse response = adminService.statsNegativeCommentOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/technician-performance")
    public ResponseEntity<?> statsTechnicianPerformance() {
        TechnicianPerformanceResponse response = adminService.statsTechnicianPerformance();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/unresolved-orders")
    public ResponseEntity<?> statsUnresolvedOrders() {
        UnresolvedOrdersResponse response = adminService.statsUnresolvedOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
