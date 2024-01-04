package com.greenfox.tribes.game.models;

import com.greenfox.tribes.misc.models.CharacterEquipment;
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
    private Long characterId;
    private Long startTime;

    @ManyToOne(fetch = FetchType.EAGER)
Activity activity;

}
