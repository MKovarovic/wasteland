package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "player")
public class WastelandUser {
  @Id @GeneratedValue private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  private String password;

  @OneToOne @ToString.Exclude private Persona persona;
}
