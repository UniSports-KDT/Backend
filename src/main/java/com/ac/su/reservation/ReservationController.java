package com.ac.su.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

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
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 에러 메시지를 JSON 형식으로 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // 예약 상태 변경(예약 승인 및 거절)
    @PutMapping("/api/reservations/{reservationId}/status")
    public ResponseEntity<?> updateReservationStatus(@PathVariable("reservationId") Long reservationId, @RequestBody Map<String, String> body) {
        String status = body.get("status");

        // 상태가 올바른지 확인
        ReservationStatus reservationStatus;
        try {
            reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 상태가 유효하지 않으면 400 Bad Request 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "유효하지 않은 상태입니다. 'APPROVED' 또는 'REJECTED'이어야 합니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // 예약 상태 변경
            Reservation updatedReservation = reservationService.updateReservationStatus(reservationId, reservationStatus);
            // JSON 형식으로 메시지 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "예약 상태(status)가 성공적으로 변경됨!");
            return ResponseEntity.ok(response); //updatedReservation
        } catch (IllegalArgumentException e) {
            // 예약이 존재하지 않을 경우
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    // userId 로 예약 조회
    @GetMapping("/api/users/{userId}/reservations")
    public ResponseEntity<?> getReservationsByUserId(@PathVariable("userId") Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        // 예약이 존재할 경우
        if (!reservations.isEmpty()) {
            return ResponseEntity.ok(reservations);
        } else {
            // 예약이 존재하지 않을 경우
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "예약이 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    // 예약 취소
    @DeleteMapping("/api/reservations/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        try {
            // 예약 취소
            reservationService.cancelReservation(reservationId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "예약이 취소되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 예약이 존재하지 않거나 이미 취소된 경우
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    // 전체 예약 조회
    @GetMapping("/api/reservations")
    public ResponseEntity<?> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        // 예약이 존재할 경우
        if (!reservations.isEmpty()) {
            return ResponseEntity.ok(reservations);
        } else {
            // 예약이 존재하지 않을 경우
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "예약이 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
