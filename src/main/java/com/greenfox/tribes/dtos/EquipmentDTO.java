package com.greenfox.tribes.dtos;

import lombok.Data;

@Data
public class EquipmentDTO {

    private String name;
    private String type;
    private Integer price;
    private Integer ATKbonus;
    private Integer DEFbonus;
    private Integer DMGbonus;
    private Integer HPbonus;
    private Integer LCKbonus;
}
