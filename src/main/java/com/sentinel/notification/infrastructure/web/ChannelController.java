package com.sentinel.notification.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.sentinel.notification.application.command.ManageChannelHandler;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.infrastructure.web.dto.ChannelRequest;
import com.sentinel.notification.infrastructure.web.dto.ChannelResponse;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ManageChannelHandler manageChannelHandler;

    public ChannelController(ManageChannelHandler manageChannelHandler) {
        this.manageChannelHandler = manageChannelHandler;
    }

    @GetMapping
    public List<ChannelResponse> getChannels() {
        return manageChannelHandler.findAll().stream()
                .map(c -> new ChannelResponse(c.id(), c.type(), c.name(), c.webhookUrl(), c.active()))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelResponse createChannel(@Valid @RequestBody ChannelRequest request) {
        NotificationChannel channel = new NotificationChannel(null, request.type(), request.name(), request.webhookUrl(), request.active());
        NotificationChannel created = manageChannelHandler.create(channel);
        return new ChannelResponse(created.id(), created.type(), created.name(), created.webhookUrl(), created.active());
    }

    @PutMapping("/{id}")
    public ChannelResponse updateChannel(@PathVariable UUID id, @Valid @RequestBody ChannelRequest request) {
        NotificationChannel channel = new NotificationChannel(null, request.type(), request.name(), request.webhookUrl(), request.active());
        NotificationChannel updated = manageChannelHandler.update(id, channel);
        return new ChannelResponse(updated.id(), updated.type(), updated.name(), updated.webhookUrl(), updated.active());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChannel(@PathVariable UUID id) {
        manageChannelHandler.delete(id);
    }

}
