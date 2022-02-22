package com.itransition.events.listeners;

import com.itransition.events.configuration.RabbitMqConfiguration;
import com.itransition.events.messages.AuthLoggerMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
@ActiveProfiles("test")
public class AuthLoggerListenerTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeAll
    public void setUp() {
        rabbitAdmin.purgeQueue(RabbitMqConfiguration.AUTH_LOGGER_QUEUE_NAME, true);
    }

    @AfterAll
    public void tearDown() {
        rabbitAdmin.purgeQueue(RabbitMqConfiguration.AUTH_LOGGER_QUEUE_NAME, true);
    }

    @Test
    public void testTwoWay() throws Exception {
        AuthLoggerListener listener = this.harness.getSpy("authLogger");
        assertNotNull(listener);

        LatchCountDownAndCallRealMethodAnswer answer = this.harness.getLatchAnswerFor("authLogger", 1);
        doAnswer(answer).when(listener).receiveMessage(any());

        AuthLoggerMessage authLoggerMessage = new AuthLoggerMessage(1L, "127.0.0.1", Instant.now().getEpochSecond());
        this.rabbitTemplate.convertAndSend(RabbitMqConfiguration.AUTH_LOGGER_QUEUE_NAME, authLoggerMessage);

        assertTrue(answer.await(10));
        verify(listener).receiveMessage(authLoggerMessage);
    }
}
