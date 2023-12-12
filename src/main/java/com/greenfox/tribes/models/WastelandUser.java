package com.greenfox.tribes.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "player")
public class WastelandUser {
  @Id @GeneratedValue private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  private String password;
}
