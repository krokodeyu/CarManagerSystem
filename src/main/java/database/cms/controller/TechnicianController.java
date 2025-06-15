package database.cms.controller;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.service.TechnicianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnicianInfoResponse> registerTechnician(
            @RequestBody TechRegisterRequest request
    ){
        TechnicianInfoResponse response = technicianService.register(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TechnicianInfoResponse>> getAllTechnicians() {
        List<TechnicianInfoResponse> responses = technicianService.getAllInfo();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{technicianId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnicianInfoResponse> updateTechnician(
            @RequestBody TechUpdateRequest request,
            @PathVariable Long technicianId
            ){
        TechnicianInfoResponse response = technicianService.update(technicianId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{technicianId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #technicianId)")
    public ResponseEntity<TechnicianInfoResponse> getTechnicianInfo(@PathVariable Long technicianId) {
        TechnicianInfoResponse response = technicianService.getInfo(technicianId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @GetMapping("/appointment/{appointmentId}/items/check")
    public ResponseEntity<List<RepairItemCheckResponse>> checkRepairItems(@PathVariable Long appointmentId){
        List<RepairItemCheckResponse> responses = technicianService.checkRepairItem(appointmentId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @PostMapping("/appointment/add-items")
    public ResponseEntity<MessageResponse> addRepairItem(@RequestBody RepairItemsAddRequest request){
        MessageResponse response = technicianService.addRepairItems(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @PutMapping("/appointment/delete-items")
    public ResponseEntity<MessageResponse> deleteRepairItem(@RequestBody RepairItemsDeleteRequest request){
        MessageResponse response = technicianService.deleteRepairItems(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @GetMapping("/appointment/{id}/check-parts")
    public ResponseEntity<List<AppointmentPartsCheckResponse>> checkAppointmentParts(@PathVariable Long id){
        List<AppointmentPartsCheckResponse> responses = technicianService.checkAppointmentParts(id);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @PostMapping("/appointment/add-parts")
    public ResponseEntity<MessageResponse> addAppointmentParts(@RequestBody AppointmentPartAddRequest request){
        MessageResponse response = technicianService.addAppointmentPart(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @PutMapping("/appointment/delete-parts")
    public ResponseEntity<MessageResponse> deleteAppointmentParts(@RequestBody AppointmentPartDeleteRequest request){
        MessageResponse response = technicianService.deleteAppointmentPart(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TECH')")
    @GetMapping("/avg-salary")
    public ResponseEntity<AvgSalaryResponse> getAvgSalary(Authentication authentication){
        AvgSalaryResponse response = technicianService.calculateAvgSalary(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


