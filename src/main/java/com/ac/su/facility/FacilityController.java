package com.ac.su.facility;

import com.ac.su.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
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

    //시설 추가
    @PostMapping("/api/facilities")
    public ResponseEntity<Facility> createFacility(@RequestBody FacilityDTO facilityDTO) {

        // 1. FacilityDTO로부터 Facility 객체 생성 및 필드 설정
        Facility facility = new Facility();
        facility.setName(facilityDTO.getName());
        facility.setDescription(facilityDTO.getDescription());
        facility.setLocation(facilityDTO.getLocation());
        facility.setOperatingHours(facilityDTO.getAvailableHours());
        facility.setFee(facilityDTO.getFee());
        facility.setAttachmentFlag(facilityDTO.getAttachmentFlag());

        // 2. 서비스 클래스의 createFacility 메서드를 호출하여 시설 및 이미지 데이터 저장
        Facility createdFacility = facilityService.createFacility(facility, facilityDTO.getAttachmentNames());

        // 3. 생성된 시설 객체를 포함한 HTTP 응답 반환
        return ResponseEntity.ok(createdFacility);
    }

    // 시설 수정
    @PutMapping("/api/facilities/{facilityId}")
    public ResponseEntity<Facility> updateFacility(@PathVariable Long facilityId, @RequestBody FacilityDTO facilityDTO) {
        // 서비스 클래스의 updateFacility 메서드를 호출하여 시설 정보 수정
        Facility updatedFacility = facilityService.updateFacility(facilityId, facilityDTO);
        // 수정된 시설 객체를 포함한 HTTP 응답 반환
        return ResponseEntity.ok(updatedFacility);
    }

    // 시설 삭제
    @DeleteMapping("/api/facilities/{facilityId}")
    public ResponseEntity<Map<String, String>> deleteFacility(@PathVariable Long facilityId) {
        // deleteFacility 메서드를 호출하여 시설 삭제
        facilityService.deleteFacility(facilityId);
        // JSON 형식으로 메시지 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 성공적으로 완료됨!");
        return ResponseEntity.ok(response);
    }

    // 전체 시설을 조회
    @GetMapping("/api/facilities")
    public ResponseEntity<List<Facility>> getAllAnnouncements() {
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }

    // 특정 시설 상세 조회
    @GetMapping("/api/facilities/{facilityId}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable("facilityId") Long facilityId) {
        Facility facility = facilityService.getFacilityById(facilityId);
        return ResponseEntity.ok(facility);
    }
}
