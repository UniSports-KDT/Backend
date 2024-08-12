package com.ac.su.reservation;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class ReservationDTO {
    private Long facilityId;  // 시설 ID
    private Long userId;      // 사용자 ID
    private LocalDate date;   // 예약 날짜
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;    // 종료 시간
}
