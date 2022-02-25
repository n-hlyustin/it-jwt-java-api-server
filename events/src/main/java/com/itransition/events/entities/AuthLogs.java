package com.itransition.events.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_logs")
public class AuthLogs implements Serializable {

    @Id
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "ip", nullable = false, length = 16)
    private String ip;

    @Column(name = "timestamp", nullable = false, length = 12)
    private Long timestamp;
}
