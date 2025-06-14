package database.cms.controller;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.entity.Appointment;
import database.cms.service.AdminService;
import database.cms.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final AppointmentService appointmentService;


    public AdminController(AdminService adminService, AppointmentService appointmentService) {
        this.adminService = adminService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserCheckResponse> checkUser(@PathVariable Long userId) {
        UserCheckResponse response = adminService.checkUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/technicians/{technicianId}")
    public ResponseEntity<TechnicianCheckResponse> checkTechnician(@PathVariable Long technicianId) {
        TechnicianCheckResponse response = adminService.checkTechnician(technicianId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/salaries")
    public ResponseEntity<SalaryRecordResponse> checkAllSalaries() {
        SalaryRecordResponse response = adminService.checkAllSalaryRecord();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleCheckResponse> checkVehicle(@PathVariable Long vehicleId) {
        VehicleCheckResponse response = adminService.checkVehicle(vehicleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderCheckResponse> checkOrder(@PathVariable Long orderId) {
        OrderCheckResponse response = adminService.checkOrder(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/records")
    public ResponseEntity<MaintenanceRecordResponse> checkAllRecords() {
        MaintenanceRecordResponse response = adminService.checkAllMaintenanceRecord();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/pay-technician")
    public ResponseEntity<?> payTechnician(@RequestBody PaymentRequest request){
        adminService.payTechnician(request.technicianId(), request.amount());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/stats/average-repair-fees")
    public ResponseEntity<?> statsAverageRepairFees(String model) {
        AverageRepairFeeResponse response = adminService.statsAverageRepairFees(model);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stats/repair-frequencies")
    public ResponseEntity<?> statsRepairFrequencies() {
        RepairFrequenciesResponse response = adminService.statsFrequencies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stats/most-frequent-failures")
    public ResponseEntity<?> statsMostFrequentFailures(@RequestParam String model) {
        MostFrequentFailuresResponse response = adminService.statsMostFrequentFailures(model);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stats/fee-proportions")
    public ResponseEntity<?> statsFeeProportions(@RequestParam Integer year, Integer month) {
        FeeProportionResponse response = adminService.statsFeeProportions(year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stats/negative-comment-orders")
    public ResponseEntity<?> statsNegativeCommentOrders() {
        NegativeCommentOrdersResponse response = adminService.statsNegativeCommentOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/stats/technician-performance")
    public ResponseEntity<?> statsTechnicianPerformance() {
        TechnicianPerformanceResponse response = adminService.statsTechnicianPerformance();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stats/unresolved-orders")
    public ResponseEntity<?> statsUnresolvedOrders() {
        UnresolvedOrdersResponse response = adminService.statsUnresolvedOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

/*
方法	路径	描述	权限
GET	/stats/vehicle-repairs	按车型统计维修次数	admin
GET	/stats/average-repair-fees	按车型同济平均维修费用	admin
GET	/stats/repair-frequencies	所有车型的维修频率	admin
GET	/stats/most-frequent-failures	特定车型最长出现的故障	admin
GET	/stats/fee-proportions	按月份统计维修费用构成	admin
GET	/stats/negative-comment-orders	筛选负面反馈工单及涉及员工	admin
GET	/stats/costs	月/季度维修费用分析	admin
GET	/stats/technician-performance	不同工种维修量统计	admin
GET	/stats/unresolved-orders	当前未完成订单列表	admin
*/
