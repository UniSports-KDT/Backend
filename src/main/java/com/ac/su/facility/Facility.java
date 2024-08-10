package com.ac.su.facility;

import com.ac.su.facilityImage.FacilityImage;
import com.ac.su.reservation.Reservation;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.List;
import java.time.LocalDateTime;

@Entity
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //시설 이름
    private String description; //시설 설명
    private String location; //시설 위치
    private String operatingHours; // 운영시간 예: "06:00-22:00"
    private Double fee;
    private LocalDateTime createdAt; // 생성 날짜
    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정 날짜

    @OneToMany(mappedBy = "facility")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "facility")
    private List<FacilityImage> images;
}