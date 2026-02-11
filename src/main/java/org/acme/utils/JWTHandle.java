package org.acme.utils;

import org.acme.entity.RoleEntity;
import org.acme.entity.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.acme.repository.RoleRepository;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class JWTHandle {
    @Inject
    RoleRepository rr;

    public String generateToken(UserEntity user) {
        List<RoleEntity> rolesByUser = rr.getRolesByUser(user.id);
        
        Set<String> roles = rolesByUser.stream()
                .map(role -> role.name)
                .collect(Collectors.toSet());

        return Jwt.issuer("https://your-app.com")
                .subject(user.username)  
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + 3600000)  // 1 hour in milliseconds
                .sign();
    }

    public String generateRefreshToken(UserEntity user) {
        List<RoleEntity> rolesByUser = rr.getRolesByUser(user.id);
        
        Set<String> roles = rolesByUser.stream()
                .map(role -> role.name)
                .collect(Collectors.toSet());

        return Jwt.issuer("https://your-app.com")
                .subject(user.username)  
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + 7200000)  // 2 hours in milliseconds
                .sign();
    }
}
