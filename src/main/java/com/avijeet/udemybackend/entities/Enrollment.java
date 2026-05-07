package com.avijeet.udemybackend.entities;

import com.avijeet.udemybackend.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
