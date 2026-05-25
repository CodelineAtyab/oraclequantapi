package om.maryam.measurement.repository;

import om.maryam.measurement.entity.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link ConversionHistory}.
 * All CRUD primitives are inherited from {@link JpaRepository}.
 */
@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {
}
