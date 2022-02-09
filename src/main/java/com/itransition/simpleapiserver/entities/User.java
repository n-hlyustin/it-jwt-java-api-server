package com.itransition.simpleapiserver.entities;

import com.itransition.simpleapiserver.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 32)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false, length = 32)
    private String firstName;

    @Column(name = "lastname", length = 32)
    private String lastName;

    public UserRole getRole() {
        return UserRole.USER;
    }
}
