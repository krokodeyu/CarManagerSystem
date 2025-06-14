package database.cms.repository;

import database.cms.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Long> {
    Optional<SparePart> findById(Long id);

    void deleteById(Long id);

    List<SparePart> findByQuantityLessThan(int quantity);

}
