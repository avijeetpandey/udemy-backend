package com.avijeet.udemybackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String minioUrl;
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}
