package com.dattran.job_finder_springboot.domain.entities;

import com.dattran.job_finder_springboot.domain.enums.UserState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "full_name")
    String fullName;

    @JsonIgnore
    String password;

    Boolean gender;

    String email;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_rel_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "is_active")
    Boolean isActive = false;

    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    String address;

    @Enumerated(EnumType.STRING)
    UserState userState;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Asset> assets = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
