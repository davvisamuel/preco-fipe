package fipe.preco.preco_fipe.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@With
@EqualsAndHashCode
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.equals(Role.ADMIN)? List.of(new SimpleGrantedAuthority(Role.ADMIN.name()), new SimpleGrantedAuthority(Role.USER.name()))
                : List.of(new SimpleGrantedAuthority(Role.USER.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
