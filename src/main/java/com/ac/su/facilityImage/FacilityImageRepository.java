package com.ac.su.facilityImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {
    void deleteByFacilityId(Long facilityId);

    // 특정 시설에 속하는 모든 이미지 조회
    List<FacilityImage> findByFacilityId(Long facilityId);
}
