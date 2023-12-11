package com.greenfox.tribes.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer ATKbonus;
    private Integer DEFbonus;
    private Integer DMGbonus;
    private Integer HPbonus;
    private Integer LCKbonus;



    @ManyToOne
    private PlayerCharacter player_character;
    }
