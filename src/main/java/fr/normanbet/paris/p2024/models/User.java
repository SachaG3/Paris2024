package fr.normanbet.paris.p2024.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @NotBlank
    @Column(length = 50,unique = true)
    private String login ;

    @Column(length = 255)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{9,}$")
    private String password ;

    @Column(length = 50)
    @NotBlank
    private String firstname ;

    @Column(length = 50)
    @NotBlank
    private String lastname;

    @Column(length = 255,unique = true)
    @Email
    private String email;

    @ManyToOne
    @NotNull
    private Role role;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO ;


    @OneToMany(mappedBy = "user")
    private List<Operation> operations=new ArrayList<>();

    @ManyToMany
    private List<OlympicElement> favorites=new ArrayList<>();

    private boolean active;
    private BigDecimal depositLimit;
    private String depositLimitPeriod;
    @Override
    public String toString() {
        return login+ " ("+email+")";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }
    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


}

