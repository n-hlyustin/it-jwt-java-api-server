package com.itransition.simpleapiserver.messages;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthLoggerMessage(@JsonProperty("userId") long userId,
                                @JsonProperty("ip") String ip,
                                @JsonProperty("timestamp") long timestamp)
        implements Serializable {
}
