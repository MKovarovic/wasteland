

package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@Entity
public class PlayerCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String characterName;
    String faction;

//    Player player;

//COMBAT STATS
    private Integer ATK; // chance to hit
    private Integer DEF; // chance to not be hit
    private Integer DMG; // how much damage is dealt in case of hit
    private Integer LCK;  //chance to crit, i.e. deal double damage
    private Integer HP;


    //INVENTORY
    private Integer Gold;

    @ToString.Exclude
    @OneToMany(mappedBy = "player_character", fetch = FetchType.EAGER)
    private ArrayList<Equipment> inventory;

//TECHNICAL STUFF
    private Boolean isBusy;
    Boolean isPremium = false;
}
