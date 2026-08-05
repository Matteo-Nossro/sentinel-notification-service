package com.sentinel.notification.infrastructure.config;

import com.sentinel.notification.domain.model.ChannelType;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.out.ChannelRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

// profil "seed" uniquement — canaux de demo, webhookUrl a remplacer avant tout envoi reel
@Component
@Profile("seed")
public class DevDataSeeder implements ApplicationRunner {

    private static final UUID CHANNEL_DISCORD = UUID.fromString("dddddddd-0000-0000-0000-000000000001");

    private final ChannelRepository channelRepository;

    public DevDataSeeder(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // idempotent sur l'ID fixe, pas sur "table vide"
        if (channelRepository.findById(CHANNEL_DISCORD).isPresent()) {
            return;
        }

        List.of(
                new NotificationChannel(CHANNEL_DISCORD, ChannelType.DISCORD, "Alertes critiques - Discord",
                        "https://discord.com/api/webhooks/PLACEHOLDER/change-me", true),
                new NotificationChannel(UUID.randomUUID(), ChannelType.TELEGRAM, "Ops - Telegram",
                        "https://api.telegram.org/botPLACEHOLDER/sendMessage", true),
                new NotificationChannel(UUID.randomUUID(), ChannelType.CUSTOM, "Webhook interne",
                        "https://example.internal/hooks/sentinel", false)
        ).forEach(channelRepository::save);
    }
}
