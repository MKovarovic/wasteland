
package com.greenfox.tribes.services;
import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MockMvc;

/*@SpringBootTest(classes = {CombatService.class})*/
public final class CombatServiceTest {

/*    @Autowired private CombatService combatService;*/

    @Test

    void testFightOutcome_AttackerWinningChance() {

/*        PersonaDTO attacker = new PersonaDTO();
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

        double winPercentage = (double) attackerWins / totalRounds * 100;
        double expectedWinPercentage = 60.0;

        Assertions.assertEquals(expectedWinPercentage, winPercentage, 1.0, "Attacker win percentage should be approximately 60%");
   */ }


}
