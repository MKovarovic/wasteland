package com.greenfox.tribes.unit.services;

import com.greenfox.tribes.services.ShopService;
import com.greenfox.tribes.dtos.EquipmentDTO;
import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.repositories.EquipmentRepository;
import com.greenfox.tribes.models.WastelandUser;
import com.greenfox.tribes.repositories.UserRepository;
import com.greenfox.tribes.repositories.CharacterEquipmentRepository;
import com.greenfox.tribes.models.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class ShopServiceTest {

  @InjectMocks private ShopService yourService;

  @Mock private EquipmentRepository equipmentRepository;

  @Mock private CharacterEquipmentRepository characterEquipmentRepository;

  @Mock private UserRepository userRepository;

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;

  @BeforeEach()
  public void setup() {
    // Setup mock SecurityContext and Authentication
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(authentication.getName()).thenReturn("test");
  }

  @Test
  public void testGetShoppingList() {
    // Setup mock UserRepository
    WastelandUser user = new WastelandUser();
    user.setUsername("test");
    user.setPersona(new Persona());
    when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

    // Setup mock EquipmentRepository and CharacterEquipmentRepository
    Equipment equipment = new Equipment();
    equipment.setName("Test Equipment");
    ArrayList<Equipment> equipmentList = new ArrayList<>();
    equipmentList.add(equipment);
    when(equipmentRepository.findAll()).thenReturn(equipmentList);

    when(characterEquipmentRepository.countAllByEquipmentAndPersona(eq(equipment), any(Persona.class)))
        .thenReturn(1);

    // Call the method to be tested
    ArrayList<EquipmentDTO> shoppingList = yourService.getShoppingList();

    // Assert the results
    assertNotNull(shoppingList);
    assertEquals(1, shoppingList.size());
    assertEquals("Test Equipment", shoppingList.get(0).getName());
    assertEquals(1, shoppingList.get(0).getNrOwned());
  }
}
