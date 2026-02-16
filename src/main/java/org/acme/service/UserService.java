package org.acme.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.acme.dto.UserLogin;
import org.acme.dto.UserRegister;
import org.acme.entity.RoleEntity;
import org.acme.entity.UserEntity;
import org.acme.entity.UserRoleEntity;
import org.acme.exception.UnauthorizedException;
import org.acme.repository.RoleRepository;
import org.acme.repository.UserRepository;
import org.acme.utils.JWTHandle;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository ur;

    @Inject
    RoleRepository rr;

    @Inject
    JWTHandle jwt;

    public record registerResponse(
        String username,
        String email
    ) {}

    public record userList(
        String username,
        String email,
        String fullname
    ){}

    public record login(
        String token,
        String refreshToken
    ) {}

    public record profile(
        Long id,
        String fullname,
        String username,
        String email,
        Set<String> roles,
        OffsetDateTime createdAt,
        OffsetDateTime modifiedAt
    ) {}

    @Transactional
    public registerResponse insert(UserRegister req) {
        UserEntity newUser = new UserEntity();

        newUser.fullname = req.fullname();
        newUser.username = req.username();
        newUser.email = req.email();
        newUser.password = BcryptUtil.bcryptHash(req.password());

        UserEntity result = ur.save(newUser);
        
        String roleName = req.getRoleOrDefault();
        Optional<RoleEntity> roleOpt = rr.findByName(roleName);
        
        if (roleOpt.isPresent()) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.userId = result.id;
            userRole.roleId = roleOpt.get().id;
            userRole.createdBy = req.username();
            rr.save(userRole);
        }
        
        return new registerResponse(result.username, result.email);
    }

    public List<userList> getAll() {
        List<UserEntity> users = ur.findAll();

        List<userList> result = new ArrayList<>();

        for(UserEntity user : users) {
            result.add(new userList(
                user.username, 
                user.email, 
                user.fullname));
        }

        return result;
    }

    public login loginUser(UserLogin req) {
        Optional<UserEntity> userOpt = ur.findByUsername(req.username());

        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("invalid credentials");
        }

        UserEntity user = userOpt.get();

        if (!BcryptUtil.matches(req.password(), user.password)) {
            throw new UnauthorizedException("invalid credentials");
        }

        return new login(jwt.generateToken(user), jwt.generateRefreshToken(user));
    }

    public profile profile(String username) {
        Optional<UserEntity> userOpt = ur.findByUsername(username);

        UserEntity user = userOpt.get();
        List<RoleEntity> userRoles = rr.getRolesByUser(user.id);

        Set<String> roles = userRoles.stream()
            .map(role -> role.name)
            .collect(Collectors.toSet());

        return new profile(
            user.id, 
            user.fullname, 
            user.username, 
            user.email, 
            roles, 
            user.createdAt, 
            user.modifiedAt
        );
    }
}
