package com.ac.su.reservation;

import com.ac.su.facility.Facility;
import com.ac.su.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date; // "2024-08-07"
    private LocalDateTime startTime; // "09:00"
    private LocalDateTime endTime; // "11:00"
    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // e.g., PENDING, APPROVED, REJECTED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //예약한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "facility_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Facility facility; //예약신청한 시설물
}