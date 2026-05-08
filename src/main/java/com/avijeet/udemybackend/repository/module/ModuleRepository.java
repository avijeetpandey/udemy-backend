package com.avijeet.udemybackend.repository.module;

import com.avijeet.udemybackend.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}
