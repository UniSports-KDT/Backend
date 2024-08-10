package com.ac.su.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    // 예약 신청
    @PostMapping("/api/reservations")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Long facilityId = reservationDTO.getFacilityId();// 시설 ID
        Long userId = reservationDTO.getUserId();// 사용자 ID
        LocalDate date = reservationDTO.getDate();// 예약 날짜
        LocalTime startTime = reservationDTO.getStartTime();// 시작 시간
        LocalTime endTime = reservationDTO.getEndTime();// 종료 시간

        try {
            // 예약 신청
            reservationService.createReservation(facilityId, userId, date, startTime, endTime);
            // JSON 형식으로 메시지 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "예약이 성공적으로 완료됨!");
            return ResponseEntity.ok(response); //
        } catch (IllegalArgumentException e) {
            // 에러 메시지를 JSON 형식으로 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
}
