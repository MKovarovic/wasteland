package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.MonsterDTO;
import com.greenfox.tribes.models.Monster;
import com.greenfox.tribes.repositories.MonsterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonsterService {
  @Autowired
  MonsterRepository monsterRepository;

  public void createMonster(String name, int hp, int atk, int dmg, int def, int lck, int gold) {
    Monster monster = new Monster();
    monster.setAtk(atk);
    monster.setDmg(dmg);
    monster.setDef(def);
    monster.setLck(lck);
    monster.setHp(hp);
    monster.setPullRing(gold);
    monster.setName(name);
    monsterRepository.save(monster);
  }

  public MonsterDTO findMonster(long id) {
    Monster monster = monsterRepository.findById(id).get();
    MonsterDTO dto = new MonsterDTO();
    dto.setId(monster.getId());
    dto.setCharacterName(monster.getName());
    dto.setAtk(monster.getAtk());
    dto.setDmg(monster.getDmg());
    dto.setDef(monster.getDef());
    dto.setLck(monster.getLck());
    dto.setHp(monster.getHp());
    dto.setPullRing(monster.getPullRing());
    return dto;
  }
}
