package fr.normanbet.paris.p2024.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    @Column(length = 50,unique = true)
    private String login ="";

    @Column(length = 255)
    private String password="";

    @Column(length = 50)
    private String firstname="";

    @Column(length = 50)
    private String lastname="";

    @Column(length = 255)
    private String email="";

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Operation> operations=new ArrayList<>();

    @ManyToMany
    private List<OlympicElement> favorites=new ArrayList<>();

    @Override
    public String toString() {
        return login+ " ("+email+")";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

