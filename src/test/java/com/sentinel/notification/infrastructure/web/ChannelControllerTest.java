package com.sentinel.notification.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.notification.application.command.ManageChannelHandler;
import com.sentinel.notification.domain.model.ChannelType;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.infrastructure.config.SecurityConfig;
import com.sentinel.notification.infrastructure.web.dto.ChannelRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
@Import(SecurityConfig.class)
class ChannelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ManageChannelHandler manageChannelHandler;

    private NotificationChannel sampleChannel() {
        return new NotificationChannel(UUID.randomUUID(), ChannelType.DISCORD,
                "alerts-discord", "https://discord.com/webhook/xxx", true);
    }

    private ChannelRequest validRequest() {
        return new ChannelRequest(ChannelType.DISCORD, "alerts-discord",
                "https://discord.com/webhook/xxx", true);
    }

    @Test
    void getChannels_shouldReturn200WithChannelList() throws Exception {
        when(manageChannelHandler.findAll()).thenReturn(List.of(sampleChannel()));

        mockMvc.perform(get("/channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("alerts-discord"))
                .andExpect(jsonPath("$[0].type").value("DISCORD"));
    }

    @Test
    void createChannel_shouldReturn201WithCreatedChannel() throws Exception {
        NotificationChannel created = sampleChannel();
        when(manageChannelHandler.create(any())).thenReturn(created);

        mockMvc.perform(post("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("alerts-discord"));
    }

    @Test
    void createChannel_shouldReturn400WhenNameIsBlank() throws Exception {
        ChannelRequest invalid = new ChannelRequest(ChannelType.DISCORD, "",
                "https://discord.com/webhook/xxx", true);

        mockMvc.perform(post("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createChannel_shouldReturn400WhenWebhookUrlIsBlank() throws Exception {
        ChannelRequest invalid = new ChannelRequest(ChannelType.DISCORD, "alerts-discord", "", true);

        mockMvc.perform(post("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteChannel_shouldReturn204AndCallHandler() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/channels/{id}", id))
                .andExpect(status().isNoContent());

        verify(manageChannelHandler).delete(id);
    }

    @Test
    void updateChannel_shouldReturn200WithUpdatedChannel() throws Exception {
        UUID id = UUID.randomUUID();
        NotificationChannel updated = new NotificationChannel(id, ChannelType.TELEGRAM,
                "alerts-telegram", "https://api.telegram.org/bot/sendMessage", true);
        when(manageChannelHandler.update(eq(id), any())).thenReturn(updated);

        ChannelRequest request = new ChannelRequest(ChannelType.TELEGRAM, "alerts-telegram",
                "https://api.telegram.org/bot/sendMessage", true);

        mockMvc.perform(put("/channels/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("TELEGRAM"));
    }
}
