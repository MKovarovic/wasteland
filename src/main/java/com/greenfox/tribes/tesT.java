package com.greenfox.tribes;

import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.services.CombatService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class tesT {
    CombatService combatService;
    @GetMapping("/test")
    public String test(){
        PersonaDTO attacker = new PersonaDTO();
        CombatantDTO defender = new CombatantDTO();
        attacker.setHp(100);
        attacker.setAtk(50);
        attacker.setDef(30);
        attacker.setDmg(20);
        attacker.setLck(60);
        defender.setHp(100);
        defender.setAtk(50);
        defender.setDef(30);
        defender.setDmg(20);
        defender.setLck(60);

        int totalRounds = 10000;
        int attackerWins = 0;

        for (int i = 0; i < totalRounds; i++) {
            Pair<CombatantDTO, CombatantDTO> outcome = combatService.fightOutcome(attacker, defender);
            if (outcome.getFirst() == attacker) {
                attackerWins++;
            }
        }
        return String.valueOf(attackerWins);
    }

}
