package com.itransition.events.repositories;

import com.itransition.events.entities.AuthLogs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthLogsRepository extends CrudRepository<AuthLogs, Long> {
}
