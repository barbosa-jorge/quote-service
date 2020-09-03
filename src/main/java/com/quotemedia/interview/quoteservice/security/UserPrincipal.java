package com.quotemedia.interview.quoteservice.security;

import com.quotemedia.interview.quoteservice.entities.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@Data
public class UserPrincipal implements UserDetails {

    private UserEntity userEntity;
    private String userId;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.userId = userEntity.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new HashSet<>();
        //Collection<AuthorityEntity> authorityEntities = new HashSet<>();

        // Get user Roles
        //Collection<RoleEntity> roles = userEntity.getRoles();

        //if(roles == null) return authorities;

//        roles.forEach((role) -> {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//            authorityEntities.addAll(role.getAuthorities());
//        });

//        authorityEntities.forEach((authorityEntity) ->{
//            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
//        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
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
        return true;
    }

}
