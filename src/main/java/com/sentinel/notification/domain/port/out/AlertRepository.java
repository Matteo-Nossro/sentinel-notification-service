package com.sentinel.notification.domain.port.out;

import com.sentinel.notification.domain.model.Alert;

import java.util.List;

public interface AlertRepository {
    Alert save(Alert alert);
    List<Alert> findAll();
}
