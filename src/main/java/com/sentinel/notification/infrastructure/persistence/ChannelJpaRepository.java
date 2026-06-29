package com.sentinel.notification.infrastructure.persistence;

import com.sentinel.notification.infrastructure.persistence.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChannelJpaRepository extends JpaRepository<ChannelEntity, UUID> {
    List<ChannelEntity> findByActiveTrue();
}
