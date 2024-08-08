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

    private String name;
    private String description;
    private String location;
    private String operatingHours; //문자로 자유롭게 받음
    private Double fee;
    private LocalDateTime createdAt; // 생성 날짜
    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정 날짜

    @OneToMany(mappedBy = "facility")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "facility")
    private List<FacilityImage> images;
}