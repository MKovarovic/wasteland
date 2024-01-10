package com.greenfox.tribes.game.dtos;

import com.greenfox.tribes.game.enums.ActivityType;
import com.greenfox.tribes.persona.models.Persona;
import lombok.Data;

@Data
public class ActivityDTO {
    private ActivityType type;
    private Long timestamp;
    private int time;
    private int pullRings;
    private Boolean givesItem;
    private Long enemyID;
    Persona personaID;

    public ActivityDTO(ActivityType type, Long timestamp, int time, int pullRings, Boolean givesItem, Long enemyID, Long personaId) {
    }
}
