package com.ac.su.facility;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
public class FacilityController {

    @Autowired
    private FacilityService facilityService;


    @GetMapping("/")
    public String test() {
        LocalTime start = LocalTime.of(9, 0);
        System.out.println(start);
        LocalTime end = start.plusHours(2);
        System.out.println(end);
        return "서버 연결 성공";
    }
    // 특정 날짜의 예약 가능한 시간대 조회
    @GetMapping("/api/facilities/{facilityId}/available-times")
    public Map<String, Object> getAvailableTimes(@PathVariable Long facilityId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date); // 요청된 날짜를 LocalDate로 변환
        System.out.println("유저가 보낸 날짜:" + date);
        return facilityService.getAvailableTimes(facilityId, localDate); // 예약 가능한 시간대 조회
    }
}
