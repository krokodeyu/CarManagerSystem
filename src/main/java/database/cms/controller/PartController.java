package database.cms.controller;

import database.cms.DTO.request.PartStoreRequest;
import database.cms.DTO.request.PartUpdateRequest;
import database.cms.DTO.response.PartsResponse;
import database.cms.DTO.response.LowStockResponse;
import database.cms.DTO.response.MessageResponse;
import database.cms.service.PartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
方法	路径	描述	权限
POST	/parts	添加配件（入库）	admin
GET	/parts	查询配件列表	admin
GET	/parts/{id}	查看配件详情	admin
PUT	/parts/{id}	更新配件信息	admin
DELETE	/parts/{id}	删除配件	admin
GET	/parts/low-stock	查询库存不足配件	admin
*/


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MessageResponse> storePart(@RequestBody PartStoreRequest request) {
        MessageResponse response =partService.storePart(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PartsResponse>> allParts() {
        List<PartsResponse> response = partService.getAllParts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PartsResponse> getPartDetail(@PathVariable Long id) {
        PartsResponse response = partService.getPartDetail(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updatePart(@RequestBody PartUpdateRequest request) {
        MessageResponse response = partService.updatePart(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePart(@PathVariable Long id) {
        MessageResponse response = partService.deletePart(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("low-stock")
    public ResponseEntity<List<LowStockResponse>> lowStockParts() {
        List<LowStockResponse> response = partService.getLowStock();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
