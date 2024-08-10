package com.ac.su.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByDateAndStartTimeAndEndTime(LocalDate date, LocalTime startTime, LocalTime endTime);


    /**
     * 주어진 시설과 날짜, 시간 범위 내에 승인된 예약이 존재하는지 확인하는 메서드임.
     * @param facilityId 조회할 시설의 ID
     * @param status 예약 상태 (예: "approved")
     * @param date 예약이 이루어진 날짜
     * @param startTime 시간 범위의 시작 시간
     * @param endTime 시간 범위의 종료 시간
     * @return 해당 시간대에 예약이 존재하면 true, 아니면 false
     */
    @Query(value = "SELECT COUNT(*) FROM reservation WHERE facility_id = :facilityId AND status = :status AND date = :date AND start_time < :endTime AND end_time > :startTime", nativeQuery = true)
    int countByFacilityIdAndStatusAndDateAndTimeRange(
            @Param("facilityId") Long facilityId,
            @Param("status") String status,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}

