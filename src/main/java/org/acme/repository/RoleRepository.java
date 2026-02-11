package org.acme.repository;

import java.util.List;
import java.util.Optional;

import org.acme.entity.RoleEntity;
import org.acme.entity.UserRoleEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RoleRepository {
    @Inject
    EntityManager em;

    @Transactional
    public UserRoleEntity save(UserRoleEntity userRole) {
        em.persist(userRole);
        return userRole;
    }

    @Transactional
    public UserRoleEntity update(UserRoleEntity userRole) {
        return em.merge(userRole);
    }

    @Transactional
    public void deleteUserRole(Integer userID, Integer roleID) {
        em.createNativeQuery("DELETE FROM user_roles WHERE user_id = :userID and role_id = :roleID")
        .setParameter("userID", userID)
        .setParameter("roleID", roleID)
        .executeUpdate();
    }

    public Optional<RoleEntity> findByID(Integer roleID) {
        try {
            RoleEntity role = (RoleEntity) em.createNativeQuery(
                "SELECT * FROM roles WHERE id = :id",
                RoleEntity.class
            )
            .setParameter("id", roleID)
            .getSingleResult();

            return Optional.of(role);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<RoleEntity> findByName(String roleName) {
        try {
            RoleEntity role = (RoleEntity) em.createNativeQuery(
                "SELECT * FROM roles WHERE name = :roleName",
                RoleEntity.class
            )
            .setParameter("roleName", roleName)
            .getSingleResult();

            return Optional.of(role);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public List<RoleEntity> getRolesByUser(Long userID) {
        return em.createNativeQuery(
            "SELECT r.* FROM roles r JOIN"
            + " user_roles rs ON r.id = rs.role_id"
            + " WHERE rs.user_id = :userID",
            RoleEntity.class
        )
        .setParameter("userID", userID)
        .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<RoleEntity> getRoles() {
        return em.createNativeQuery(
            "SELECT * FROM roles",
            RoleEntity.class
        ).getResultList();
    }
}
