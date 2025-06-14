package database.cms.repository;

import database.cms.entity.RepairItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairItemRepository extends JpaRepository<RepairItem, Long> {

}
