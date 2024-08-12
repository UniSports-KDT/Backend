package com.ac.su.reservation;

import com.ac.su.facility.Facility;
import com.ac.su.facility.FacilityService;
import com.ac.su.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 예약 생성
    public Reservation createReservation(Long facilityId, Long userId, LocalDate date, LocalTime startTime, LocalTime endTime) {

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
    }
}
