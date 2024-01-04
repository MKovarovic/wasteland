package com.greenfox.tribes.game.models;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.persona.models.Persona;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @OneToOne
    @JoinColumn(name = "activity_id")
    Activity activity;

    @OneToOne
    @JoinColumn(name = "persona_id")
    Persona persona;



}
