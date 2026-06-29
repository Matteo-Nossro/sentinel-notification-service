package com.sentinel.notification.infrastructure.persistence;

import com.sentinel.notification.infrastructure.persistence.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, UUID> {}
