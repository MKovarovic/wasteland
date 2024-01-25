package com.greenfox.tribes;

import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import com.greenfox.tribes.services.CombatService;
import com.greenfox.tribes.services.PersonaService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class tesT {
    CombatService combatService;
    PersonaService characterService;
    @GetMapping("/test")
    public String test(){
        PersonaDTO attacker = new PersonaDTO();
        CombatantDTO defender = new CombatantDTO();
        attacker.setHp(50);
        attacker.setAtk(50);
        attacker.setDef(10);
        attacker.setDmg(10);
        attacker.setLck(10);
        defender.setHp(50);
        defender.setAtk(50);
        defender.setDef(10);
        defender.setDmg(10);
        defender.setLck(10);

       /* PersonaDTO attacker2 = characterService.readCharacter(1L);
        CombatantDTO defender2 = characterService.readCharacter(2L);*/

        int totalRounds = 10000;
        int attackerWins = 0;

        for (int i = 0; i < totalRounds; i++) {
            PersonaDTO attacker2 = characterService.readCharacter(1L);
            PersonaDTO defender2 = characterService.readCharacter(2L);
/*            attacker.setHp(50);
            defender.setHp(50);*/
            Pair<PersonaDTO, PersonaDTO> outcome = combatService.fightOutcome(attacker2, defender2);
            if (outcome.getFirst() == attacker2) {
                attackerWins++;
            }
        }
        return String.valueOf(attackerWins);
    }

}
