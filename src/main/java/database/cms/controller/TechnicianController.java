package database.cms.controller;

import database.cms.DTO.request.TechRegisterRequest;
import database.cms.DTO.request.TechUpdateRequest;
import database.cms.DTO.response.ReminderResponse;
import database.cms.DTO.response.TechnicianInfoResponse;
import database.cms.service.TechnicianService;
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
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TechnicianInfoResponse>> getAllTechnicians() {
        List<TechnicianInfoResponse> responses = technicianService.getAllInfo();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{technicianId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnicianInfoResponse> updateTechnician(
            @RequestBody TechUpdateRequest request,
            @PathVariable Long technicianId
            ){
        TechnicianInfoResponse response = technicianService.update(technicianId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{technicianId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #technicianId)")
    public ResponseEntity<TechnicianInfoResponse> getTechnicianInfo(@PathVariable Long technicianId) {
        TechnicianInfoResponse response = technicianService.getInfo(technicianId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<List<ReminderResponse>> checkReminders(Authentication authentication){
        List<ReminderResponse> responses = technicianService.checkReminders(authentication);
        return ResponseEntity.ok(responses);
    }
}
