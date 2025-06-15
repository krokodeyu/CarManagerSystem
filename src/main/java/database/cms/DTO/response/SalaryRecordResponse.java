package database.cms.DTO.response;

import database.cms.entity.SalaryRecord;

import java.util.List;

public record SalaryRecordResponse (
        Long id,
        Long techId,
        Double amount
){
}
