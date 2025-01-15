package adeo.leroymerlin.cdp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.entity.Event;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Long> {
	void deleteById(Long eventId);
}
