package com.ac.su.facility;

import com.ac.su.facilityImage.FacilityImage;
import com.ac.su.facilityImage.FacilityImageRepository;
import com.ac.su.reservation.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacilityService {

    // ReservationRepository를 자동으로 주입받습니다.
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private FacilityImageRepository facilityImageRepository;

    //특정 날짜에 시설의 예약 가능한 시간대를 조회하는 메서드.
    public Map<String, Object> getAvailableTimes(Long facilityId, LocalDate date) {
        // 예약 가능한 시간대를 저장할 리스트.
        List<Map<String, Object>> availableTimes = new ArrayList<>();

        // 예약 조회를 시작할 시간: 09:00
        LocalTime start = LocalTime.of(6, 0); // 06:00

        // 각 시간대는 2시간 단위로 설정.
        LocalTime end = start.plusHours(2); // 08:00

        // 09:00부터 22:00까지 2시간 단위로 예약 가능한 시간대를 조회.
        while (start.isBefore(LocalTime.of(22, 0))) {
            // 현재 시간대에 이미 승인된 예약이 있는지 확인.
            // 예약이 없는 경우 available은 true, 있는 경우 false로 설정.
            boolean available = reservationRepository.countByFacilityIdAndStatusAndDateAndTimeRange(
                    facilityId, "approved", date, start, end) == 0;

            // 시간대와 예약 가능 여부를 저장할 Map 객체를 생성.
            Map<String, Object> timeSlot = new HashMap<>();
            timeSlot.put("startTime", start.toString()); // 시간대의 시작 시간을 문자열로 저장.
            timeSlot.put("endTime", end.toString()); // 시간대의 종료 시간을 문자열로 저장.
            timeSlot.put("available", available); // 해당 시간대의 예약 가능 여부를 저장.
            availableTimes.add(timeSlot); // 리스트에 시간대를 추가.

            // 다음 시간대의 시작 시간을 현재 종료 시간으로 갱신.
            start = end;
            // 종료 시간은 새로운 시작 시간으로부터 2시간 후로 설정.
            end = start.plusHours(2);
        }

        // 결과를 담을 최종 응답 객체를 생성.
        Map<String, Object> response = new HashMap<>();
        response.put("facilityId", facilityId); // 조회한 시설의 ID를 저장.
        response.put("date", date.toString()); // 조회한 날짜를 문자열로 저장.
        response.put("availableTimes", availableTimes); // 예약 가능한 시간대 리스트를 저장.

        // 최종 결과를 반환.
        return response;
    }

    //새로운 시설을 생성하고, 해당 시설에 이미지가 있는 경우 이미지를 저장하는 메서드
    @Transactional
    public Facility createFacility(Facility facility, List<String> attachmentNames) {
        // 1. Facility 객체를 먼저 데이터베이스에 저장
        Facility savedFacility = facilityRepository.save(facility);

        // 2. attachmentFlag가 Y이고, attachmentNames 리스트가 비어 있지 않다면 이미지 저장 로직을 실행
        if (facility.getAttachmentFlag() == AttachmentFlag.Y && attachmentNames != null && !attachmentNames.isEmpty()) {
            for (String imageUrl : attachmentNames) {
                FacilityImage facilityImage = new FacilityImage();
                facilityImage.setImageUrl(imageUrl); // 유저가 보낸 이미지 URL을 설정
                facilityImage.setFacility(savedFacility); // 해당 이미지를 생성한 시설과 연관
                facilityImageRepository.save(facilityImage); // 이미지 데이터를 데이터베이스에 저장
            }
        }

        // 3. 최종적으로 생성된 Facility 객체를 반환
        return savedFacility;
    }
}
