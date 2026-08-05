package com.mikedevcol.restaurantapi.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "phone_number_prefixes")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberPrefix {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 10)
  private String prefix;

  @Column(nullable = false, length = 50)
  private String country;

}
