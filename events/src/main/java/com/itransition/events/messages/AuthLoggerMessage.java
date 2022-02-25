package com.itransition.events.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record AuthLoggerMessage(@JsonProperty("userId") long userId,
                                @JsonProperty("ip") String ip,
                                @JsonProperty("timestamp") long timestamp)
        implements Serializable {
}
