package com.academicrepo.back.academic_repo.auth.domain.entities;

import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DAuthenticatedUser implements UserDetails {

    private Long id;
    private String email;
    private String passwordHash;
    private Boolean isActive;

    public static DAuthenticatedUser fromDUser(DUser user) {
        DAuthenticatedUser authUser = new DAuthenticatedUser();
        authUser.setId(user.getId());
        authUser.setEmail(user.getEmail());
        authUser.setPasswordHash(user.getPasswordHash());
        authUser.setIsActive(user.getIsActive());
        return authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
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
        return isActive != null && isActive;
    }
}
