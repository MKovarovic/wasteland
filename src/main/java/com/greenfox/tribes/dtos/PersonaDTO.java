package com.greenfox.tribes.dtos;

import com.greenfox.tribes.mappers.EquipmentMapping;
import com.greenfox.tribes.models.CharacterEquipment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.util.Pair;


import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonaDTO extends CombatantDTO {

  private Long id;

  private String characterName;
  private String faction;

  private int atk; // chance to hit
  private int def; // chance to not be hit
  private int dmg; // how much damage is dealt in case of hit
  private int lck; // chance to crit, i.e. deal double damage
  private int hp;

  private int pullRing;
  private List<Pair<Long, EquipmentDTO>> inventory = new ArrayList<>();
  private List<EquipmentDTO> equipedItems = new ArrayList<>();

  public void setInventory(List<CharacterEquipment> bundle) {
    for (CharacterEquipment e : bundle) {
      EquipmentDTO equipmentDTO = EquipmentMapping.remap(e.getEquipment());
    inventory.add(Pair.of(e.getId(), equipmentDTO));
  }
  }

  public void setInventoryEquipment(List<EquipmentDTO> bundle) {
    equipedItems = new ArrayList<>(bundle);
  }

  public void setEquippedItemsEquipment(List<EquipmentDTO> bundle) {
    equipedItems = new ArrayList<>(bundle);
  }

  public void setEquipedItems(List<CharacterEquipment> bundle) {
    for (CharacterEquipment e : bundle) {
      if (e.getIsEquipped()) {
        EquipmentDTO equipmentDTO = EquipmentMapping.remap(e.getEquipment());
        equipedItems.add(equipmentDTO);
      }
    }
  }
}
