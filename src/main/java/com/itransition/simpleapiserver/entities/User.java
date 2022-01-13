package com.itransition.simpleapiserver.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
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

    public String getRole() {
        return "User";
    }
}
