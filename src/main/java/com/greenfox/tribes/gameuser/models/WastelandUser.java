package com.greenfox.tribes.gameuser.models;

import com.greenfox.tribes.persona.models.Persona;
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

  @OneToOne
  @ToString.Exclude
  private Persona persona;
}
