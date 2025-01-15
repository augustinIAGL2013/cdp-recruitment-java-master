package adeo.leroymerlin.cdp.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.entity.Band;

@Repository
@Transactional
public interface BandRepository extends JpaRepository<Band, Long> {
	void deleteById(Long bandId);

	Set<Band> findByNameIn(List<String> bandNames);
}
