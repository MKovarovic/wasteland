package com.greenfox.tribes.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greenfox.tribes.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class CharacterUIControllerIntegrationTest extends BaseIntegrationTest {
  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser(
      username = "test",
      authorities = {})
  void userLogsIn() throws Exception {
    // basic test example
    mockMvc.perform(get("/character/me")).andExpect(status().isOk()).andReturn();
  }
}
