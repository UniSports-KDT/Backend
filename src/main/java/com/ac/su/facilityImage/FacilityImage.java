package com.ac.su.facilityImage;

import com.ac.su.facility.Facility;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FacilityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; //이미지 URL
    private String description; //이미지 설명 ex> 체육관1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "facility_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Facility facility;
}