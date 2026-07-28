package com.mikedevcol.restaurantapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.mikedevcol.restaurantapi.configuration.TestContainerConfig;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.repositories.PhoneNumberPrefixRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Import(TestContainerConfig.class)
class OwnerControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PhoneNumberPrefixRepository phoneNumberPrefixRepository;

  private Long prefixId;

  @BeforeEach
  void setUp() {
    PhoneNumberPrefix prefix = phoneNumberPrefixRepository.save(new PhoneNumberPrefix(null, "+58", "Venezuela"));
    prefixId = prefix.getId();
  }

  @Test
  void createShouldPersistOwnerAggregateWithCascade() throws Exception {
    String payload = """
        {
          "firstName": "Ana",
          "lastName": "Perez",
          "contactInfo": {
            "email": "owner-aggregate@test.com",
            "phoneNumber": "3110001000",
            "phoneNumberPrefixId": %d
          },
          "restaurants": [
            { "code": "RST100" },
            { "code": "RST101" }
          ]
        }
        """.formatted(prefixId);

    mockMvc.perform(post("/api/v1/owners")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.contactInfo.id").isNumber())
        .andExpect(jsonPath("$.restaurants.length()").value(2))
        .andExpect(jsonPath("$.restaurants[0].ownerId").isNumber());
  }
}
