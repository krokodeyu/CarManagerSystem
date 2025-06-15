package database.cms.controller;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.entity.Appointment;
import database.cms.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/*预约管理 `/appointments`

| 方法       | 路径                           | 描述        | 权限    |
| -------- | ---------------------------- | --------- | ----- |
| `POST`   | `/appointments`              | 创建预约      | user  |
| `GET`    | `/appointments`              | 查询全部预约    | admin |
| `GET`    | `/appointments/{id}`         | 查看预约详情    | all   |
| `PUT`    | `/appointments/{id}`         | 修改预约信息    | user  |
| `DELETE` | `/appointments/{id}`         | 取消预约      | user  |
| `POST`   | `/appointments/{id}/confirm` | 确认预约并生成订单 | tech  |

---*/


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("@securityService.isVehicleOwner(authentication, #request.vehicleId())")
    @PostMapping("/create")
    public ResponseEntity<AppointmentResponse> createAppointment (@RequestBody AppointmentRequest request, Authentication authentication){
        AppointmentResponse response = appointmentService.createAppointment(request, authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<AllAppointmentResponse> getAppointment (){
        AllAppointmentResponse response = appointmentService.getAllAppointment();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-detail/{appointmentId}")
    public ResponseEntity<AppointmentDetailResponse> getDetail (@PathVariable Long appointmentId){
        AppointmentDetailResponse response = appointmentService.getAppointmentDetail(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cancel")
    public ResponseEntity<AppointmentCancelResponse> cancelAppointment (@RequestBody AppointmentCancelRequest request){
        AppointmentCancelResponse response = appointmentService.cancelAppointment(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @PostMapping("/confirm")
    public ResponseEntity<AppointmentConfirmationResponse> confirmAppointment (@RequestBody AppointmentConfirmationRequest request, Authentication authentication){
        AppointmentConfirmationResponse response = appointmentService.confirmAppointment(request, authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/reminder")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderUser(authentication, #orderId)")
    public ResponseEntity<MessageResponse> remindAppointment(
            @PathVariable Long orderId
    ) {
        MessageResponse response = appointmentService.remind(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/check-by-status")
    public ResponseEntity<?> checkAppointmentByStatus(@RequestParam Appointment.Status status) {
        AppointmentByStatusResponse response = appointmentService.getAppointmentByStatus(status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/arrange")
    public ResponseEntity<?> arrangeAppointment(@RequestBody AppointmentArrangementRequest request){
        appointmentService.arrangeAppointment(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @PostMapping("/finish")
    public ResponseEntity<AppointmentFinishResponse> finishAppointment(@RequestBody AppointmentFinishRequest request){
        AppointmentFinishResponse response = appointmentService.finishAppointment(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
