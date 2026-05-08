package com.avijeet.udemybackend.repository.video;

import com.avijeet.udemybackend.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
