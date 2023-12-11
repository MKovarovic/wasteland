package com.greenfox.tribes.models.dtos;

import com.greenfox.tribes.models.Equipment;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CharacterDTO {

    Long id;

    String characterName;
    String faction;

    private Integer ATK; // chance to hit
    private Integer DEF; // chance to not be hit
    private Integer DMG; // how much damage is dealt in case of hit
    private Integer LCK;  //chance to crit, i.e. deal double damage
    private Integer HP;

    private Integer Gold;

    private ArrayList<Equipment> inventory;

}
