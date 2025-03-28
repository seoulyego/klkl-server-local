package taco.klkl.domain.region.dao.region;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.region.domain.region.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
	Region findFirstByName(final String name);

	List<Region> findAllByOrderByIdAsc();
}
