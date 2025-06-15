package database.cms.service;

import database.cms.DTO.request.PartStoreRequest;
import database.cms.DTO.request.PartUpdateRequest;
import database.cms.DTO.response.PartsResponse;
import database.cms.DTO.response.LowStockResponse;
import database.cms.DTO.response.MessageResponse;
import database.cms.entity.SparePart;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.SparePartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

@Service
public class PartService {
    private final SparePartRepository sparePartRepository;

    public PartService(SparePartRepository sparePartRepository) {
        this.sparePartRepository = sparePartRepository;
    }

    public MessageResponse storePart(PartStoreRequest request) {

        SparePart part = new SparePart();
        part.setName(request.name());
        part.setPrice(request.price());
        part.setQuantity(request.quantity());
        sparePartRepository.save(part);

        return new MessageResponse("Successfully added part");

    }

    public List<PartsResponse> getAllParts() {

        List<SparePart> spareParts = sparePartRepository.findAll();
        List<PartsResponse> partsResponses = new ArrayList<>();
        for (SparePart p : spareParts) {
            PartsResponse response = new PartsResponse(
                 p.getId(),
                 p.getName(),
                 p.getPrice(),
                 p.getQuantity(),
                 p.getCreatedAt()
            );
            partsResponses.add(response);
        }
        return partsResponses;
    }

    public PartsResponse getPartDetail(Long partId) {

        SparePart sparePart = sparePartRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("PART_NOT_FOUND", "配件不存在"));

        return new PartsResponse(
                sparePart.getId(),
                sparePart.getName(),
                sparePart.getPrice(),
                sparePart.getQuantity(),
                sparePart.getCreatedAt()
        );
    }

    public MessageResponse updatePart(PartUpdateRequest request) {
        SparePart OriginalPart = sparePartRepository.findById(request.partId())
                .orElseThrow(()-> new ResourceNotFoundException("PART_NOT_FOUND", "配件不存在"));

        if(request.name().isPresent()) OriginalPart.setName(request.name().get());
        if(request.price().isPresent()) OriginalPart.setPrice(request.price().get());
        if(request.quantity().isPresent()) OriginalPart.setQuantity(request.quantity().get());
        sparePartRepository.save(OriginalPart);

        return new MessageResponse("Successfully updated part!");
    }

    public MessageResponse deletePart(Long partId) {
        sparePartRepository.deleteById(partId);
        return new MessageResponse("Successfully deleted part!");
    }

    public List<LowStockResponse> getLowStock() {
        List<SparePart> lowStock = sparePartRepository.findByQuantityLessThan(20);
        List<LowStockResponse> lowStockResponses = new ArrayList<>();
        for (SparePart p : lowStock) {
            LowStockResponse response = new LowStockResponse(
                    p.getId(),
                    p.getName(),
                    p.getQuantity()
            );
            lowStockResponses.add(response);
        }
        return lowStockResponses;

    }

}
