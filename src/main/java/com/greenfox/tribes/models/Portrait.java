package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Portrait {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String head;
  private String hair;
  private String eyes;
  private String eyebrows;
  private String nose;
  private String mouth;

  @OneToOne
  @JoinColumn(name = "persona_id")
  Persona persona;
}
