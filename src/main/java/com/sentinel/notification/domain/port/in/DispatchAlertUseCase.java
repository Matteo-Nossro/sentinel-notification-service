package com.sentinel.notification.domain.port.in;

import com.sentinel.notification.domain.model.Alert;

import java.util.List;

public interface DispatchAlertUseCase {
    void dispatch(Alert alert);
    List<Alert> getHistory();
}
