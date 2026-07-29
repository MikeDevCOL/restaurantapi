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
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mikedevcol.restaurantapi.configuration.TestContainerConfig;
import com.mikedevcol.restaurantapi.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.repositories.PhoneNumberPrefixRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Import(TestContainerConfig.class)
class OwnerContactInfoControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PhoneNumberPrefixRepository phoneNumberPrefixRepository;

  private Long prefixId;

  @BeforeEach
  void setUp() {
    PhoneNumberPrefix prefix = phoneNumberPrefixRepository.save(new PhoneNumberPrefix(null, "+57", "Colombia"));
    prefixId = prefix.getId();
  }

  @Test
  void createShouldReturnCreated() throws Exception {
    long nonce = Math.abs(System.nanoTime());
    String uniquePhoneNumber = String.format("3%09d", nonce % 1_000_000_000L);

    String payload = """
        {
          "phoneNumber": "%s",
          "phoneNumberPrefixId": %d
        }
        """.formatted(uniquePhoneNumber, prefixId);

    mockMvc.perform(post("/api/v1/owner-contact-info")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.phoneNumberPrefix.id").value(prefixId));
  }
}
