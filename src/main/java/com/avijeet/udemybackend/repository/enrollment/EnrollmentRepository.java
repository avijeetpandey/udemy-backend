package com.avijeet.udemybackend.repository.enrollment;

import com.avijeet.udemybackend.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
