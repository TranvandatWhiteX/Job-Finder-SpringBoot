package com.dattran.job_finder_springboot.domain.entities;

import com.dattran.job_finder_springboot.domain.enums.UserState;
import com.fasterxml.jackson.annotation.*;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseEntity implements UserDetails, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "full_name")
    String fullName;

    @JsonIgnore
    String password;

    Boolean gender;

    String email;

    @Column(name = "temp_password")
    String tempPassword;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "tbl_rel_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "is_active")
    Boolean isActive = false;

    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    UserState userState;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Address address;

    String companyId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        roles.forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_"+role.getName())));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getName() {
        return email;
    }
}
