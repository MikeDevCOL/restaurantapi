package com.mikedevcol.restaurantapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

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
import com.mikedevcol.restaurantapi.models.Owner;
import com.mikedevcol.restaurantapi.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.repositories.OwnerRepository;
import com.mikedevcol.restaurantapi.repositories.PhoneNumberPrefixRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Import(TestContainerConfig.class)
class RestaurantControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private OwnerRepository ownerRepository;

  @Autowired
  private PhoneNumberPrefixRepository phoneNumberPrefixRepository;

  private Long ownerId;

  @BeforeEach
  void setUp() {
    PhoneNumberPrefix prefix = phoneNumberPrefixRepository.save(new PhoneNumberPrefix(null, "+57", "Colombia"));
    OwnerContactInfo info = OwnerContactInfo.builder()
        .email("restaurant-owner@test.com")
        .phoneNumber("3005550000")
        .phoneNumberPrefix(prefix)
        .build();

    Owner owner = Owner.builder()
        .firstName("Carlos")
        .lastName("Mora")
        .restaurants(Set.of())
        .build();
    owner.assignContactInfo(info);

    ownerId = ownerRepository.save(owner).getId();
  }

  @Test
  void createShouldReturnCreated() throws Exception {
    String payload = """
        {
          "code": "RST900",
          "ownerId": %d
        }
        """.formatted(ownerId);

    mockMvc.perform(post("/api/v1/restaurants")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.code").value("RST900"))
        .andExpect(jsonPath("$.ownerId").value(ownerId));
  }
}
