package com.mikedevcol.restaurantapi.core.models;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.mikedevcol.restaurantapi.security.models.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "owners")
@Entity
@ToString
public class Owner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @ToString.Exclude
  @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Builder.Default
  private Set<Restaurant> restaurants = new LinkedHashSet<>();

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "contact_info_id", nullable = false, unique = true)
  private OwnerContactInfo contactInfo;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  public void assignContactInfo(OwnerContactInfo contactInfo) {
    this.contactInfo = Objects.requireNonNull(contactInfo, "contactInfo is required");
  }

  public void addRestaurant(Restaurant restaurant) {
    Restaurant value = Objects.requireNonNull(restaurant, "restaurant is required");
    value.setOwner(this);
    restaurants.add(value);
  }

}
