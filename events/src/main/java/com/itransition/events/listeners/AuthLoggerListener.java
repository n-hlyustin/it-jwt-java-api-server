package com.itransition.events.listeners;

import com.itransition.events.configuration.RabbitMqConfiguration;
import com.itransition.events.entities.AuthLogs;
import com.itransition.events.mappers.AuthLogsMapper;
import com.itransition.events.messages.AuthLoggerMessage;
import com.itransition.events.repositories.AuthLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoggerListener {
    private final AuthLogsRepository authLogsRepository;

    @RabbitListener(id = "authLogger", queues = RabbitMqConfiguration.AUTH_LOGGER_QUEUE_NAME)
    public void receiveMessage(final AuthLoggerMessage authLoggerMessage) {
        AuthLogs authLogs = AuthLogsMapper.INSTANCE.authMessageToEntity(authLoggerMessage);
        authLogsRepository.save(authLogs);
    }
}
