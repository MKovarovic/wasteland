package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

@Entity
@Data
public class PlayerEquipment {
  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(name = "character_id")
  PlayerCharacter playerCharacter;

  @ManyToOne
  @JoinColumn(name = "equipment_id")
  Equipment equipment;
}
