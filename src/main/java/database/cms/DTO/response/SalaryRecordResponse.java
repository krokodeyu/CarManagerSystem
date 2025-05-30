package database.cms.DTO.response;

import database.cms.entity.SalaryRecord;

import java.util.List;

public record SalaryRecordResponse (
        List<SalaryRecord> salaryRecords
){
}
