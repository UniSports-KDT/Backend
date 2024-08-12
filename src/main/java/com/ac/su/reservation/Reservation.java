package com.ac.su.reservation;

import com.ac.su.facility.Facility;
import com.ac.su.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date; // "2024-08-07"
    private LocalTime startTime; // "09:00"
    private LocalTime endTime; // "11:00"
    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // e.g., PENDING, APPROVED, REJECTED

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user; //예약한 유저

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "facility_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Facility facility; //예약신청한 시설물
}