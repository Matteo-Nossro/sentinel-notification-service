package com.sentinel.notification.infrastructure.persistence;

import com.sentinel.notification.infrastructure.persistence.entity.AttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttemptJpaRepository extends JpaRepository<AttemptEntity, UUID> {}
