package com.itransition.events.mappers;

import com.itransition.events.entities.AuthLogs;
import com.itransition.events.messages.AuthLoggerMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthLogsMapper {
    AuthLogsMapper INSTANCE = Mappers.getMapper(AuthLogsMapper.class);

    @Mapping(target = "userId")
    AuthLogs authMessageToEntity(AuthLoggerMessage authLoggerMessage);
}
