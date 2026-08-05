package com.mikedevcol.restaurantapi.core.controllers;

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

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import com.mikedevcol.restaurantapi.configuration.TestContainerConfig;
import com.mikedevcol.restaurantapi.core.models.Owner;
import com.mikedevcol.restaurantapi.core.models.OwnerContactInfo;
import com.mikedevcol.restaurantapi.core.models.PhoneNumberPrefix;
import com.mikedevcol.restaurantapi.core.repositories.OwnerRepository;
import com.mikedevcol.restaurantapi.core.repositories.PhoneNumberPrefixRepository;
import com.mikedevcol.restaurantapi.security.models.Role;
import com.mikedevcol.restaurantapi.security.models.User;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Import(TestContainerConfig.class)
@Transactional
class RestaurantControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private OwnerRepository ownerRepository;

  @Autowired
  private PhoneNumberPrefixRepository phoneNumberPrefixRepository;

  @Autowired
  private EntityManager entityManager;

  private Long ownerId;

  @BeforeEach
  void setUp() {
    long nonce = Math.abs(System.nanoTime());

    PhoneNumberPrefix prefix = phoneNumberPrefixRepository.save(new PhoneNumberPrefix(null, "+57", "Colombia"));
    OwnerContactInfo info = OwnerContactInfo.builder()
        .phoneNumber("3005550000")
        .phoneNumberPrefix(prefix)
        .build();

    Role role = new Role();
    role.setName("OWNER");

    // TODO: Consider using a RoleRepository to persist the role instead of directly
    // * using the EntityManager.
    entityManager.persist(role);

    User user = new User();
    user.setUsername("owner_" + nonce);
    user.setEmail("owner_" + nonce + "@example.com");
    user.setPassword("test-password");
    user.setRole(role);

    // TODO: Persist the user entity to the database using the EntityManager
    entityManager.persist(user);

    Owner owner = Owner.builder()
        .firstName("Carlos")
        .lastName("Mora")
        .user(user)
        .restaurants(Set.of())
        .build();
    owner.assignContactInfo(info);

    ownerId = ownerRepository.save(owner).getId();
  }

  @Test
  void createShouldReturnCreated() throws Exception {
    long nonce = Math.abs(System.nanoTime());
    String uniqueRestaurantCode = String.format("R%09d", nonce % 1_000_000_000L);

    String payload = """
        {
          "code": "%s",
          "ownerId": %d
        }
        """.formatted(uniqueRestaurantCode, ownerId);

    mockMvc.perform(post("/api/v1/restaurants")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.code").value(uniqueRestaurantCode))
        .andExpect(jsonPath("$.ownerId").value(ownerId));
  }
}
