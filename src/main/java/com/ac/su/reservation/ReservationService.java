package com.ac.su.reservation;

import com.ac.su.facility.Facility;
import com.ac.su.facility.FacilityService;
import com.ac.su.redis.RedisLockService;
import com.ac.su.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private RedisLockService redisLockService;
    private static final long LOCK_TIMEOUT = 10L; // 락 타임아웃 10초

    // 예약 생성
    public Reservation createReservation(Long facilityId, Long userId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // 락 키 생성
        String lockKey = facilityId + ":" + date.toString() + ":" + startTime.toString() + "-" + endTime.toString();
        // 락 획득
        if (redisLockService.acquireLock(lockKey, LOCK_TIMEOUT)) {
            // 락 획득 성공 시 메시지 출력
            System.out.println("락을 성공적으로 획득했습니다. 예약 처리를 시작합니다.");
        } else {
            // 락 획득 실패 시 예외 발생
            throw new IllegalArgumentException("다른 사용자가 동일한 시간대에 예약을 시도 중입니다. 잠시 후 다시 시도하세요.");
        }

        try {
            List<Map<String, Object>> availableTimes = (List<Map<String, Object>>) facilityService.getAvailableTimes(facilityId, date).get("availableTimes");

            if (!availableTimes.contains(Map.of("startTime", startTime.toString(), "endTime", endTime.toString(), "available", true))) {
                throw new IllegalArgumentException("이미 예약된 시간대입니다"); // 중복 예약 확인
            }

            //status가 "PENDING"이면 예약처리 중인 시간이니까 예약을 막아야함
            var existingReservation = reservationRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime);
            if (existingReservation.isPresent()) {
                if (existingReservation.get().getStatus().toString().equals("PENDING")) {
                    throw new IllegalArgumentException("이미 예약된 시간대입니다22");
                }
            }

            //선택한 시설물 객체 생성
            Facility facility = new Facility();
            facility.setId(facilityId);
            //예약하는 유저 객체 생성
            User user = new User();
            user.setId(userId);

            // 예약 생성
            Reservation reservation = new Reservation();
            reservation.setFacility(facility); // 시설 정보 설정
            reservation.setUser(user); // 사용자 ID 설정
            reservation.setDate(date); // 예약 날짜 설정
            reservation.setStartTime(startTime); // 시작 시간 설정
            reservation.setEndTime(endTime); // 종료 시간 설정
            reservation.setStatus(ReservationStatus.PENDING); // 예약 상태 설정

            return reservationRepository.save(reservation); // 예약 저장

        } finally {
            //락 해제
            redisLockService.releaseLock(lockKey);
        }
    }
    // 예약 상태 변경
    public Reservation updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        reservation.setStatus(status);
        reservationRepository.save(reservation);

        // 변경된 예약 저장
        return reservationRepository.save(reservation);
    }
    // 예약 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));
        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
    }

}
