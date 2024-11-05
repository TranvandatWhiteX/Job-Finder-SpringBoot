package com.dattran.job_finder_springboot.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "tbl_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    String description;

    @ManyToMany(mappedBy = "roles")
    Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
