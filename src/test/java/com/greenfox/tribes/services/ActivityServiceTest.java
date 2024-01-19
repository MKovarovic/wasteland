/*
package com.greenfox.tribes.services;

import com.greenfox.tribes.dtos.CombatantDTO;
import com.greenfox.tribes.dtos.PersonaDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class ActivityServiceTest {
  ActivityService activityService;

  @Test
  void fightOutcomeChances() {
    int totalIterations = 10000; // Adjust the number of iterations based on your requirements
    Map<String, Integer> orderCount = new HashMap<>();

    for (int i = 0; i < totalIterations; i++) {
      CombatantDTO attacker = createCombatantDTO(1L);
      CombatantDTO defender = createCombatantDTO(2L);

      CombatantDTO[] result = activityService.fightOutcome((PersonaDTO) attacker, defender);

      String orderKey = result[0] == attacker ? "attackerFirst" : "defenderFirst";
      orderCount.put(orderKey, orderCount.getOrDefault(orderKey, 0) + 1);
    }

    // Check that the orders are approximately equal
    int tolerance = totalIterations / 20; // Allow a 5% difference
    assertEquals(orderCount.get("attackerFirst"), orderCount.get("defenderFirst"), tolerance);
  }

  // Helper method to create a CombatantDTO with appropriate values
  private CombatantDTO createCombatantDTO(Long id) {
    // Implement based on your needs
    return new CombatantDTO(id, "test", 10, 30, 10, 16, 10, 100);
  }

  }
}
*/
