package com.ac.su.facilityImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {
    void deleteByFacilityId(Long facilityId);
}
