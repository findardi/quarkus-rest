package org.acme.repository;

import org.acme.entity.UserEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {
    @Inject
    EntityManager em;

    @Transactional
    public UserEntity save(UserEntity user) {
        em.persist(user);
        return user;
    }

    @Transactional
    public UserEntity update(UserEntity user) {
        return em.merge(user);
    }

    @Transactional
    public void delete(Long id) {
        em.createNativeQuery(
            "DELETE FROM users WHERE id = :id"
        )
        .setParameter("id", id)
        .executeUpdate();
    }

    public Optional<UserEntity> findById(Long id) {
        try {
            UserEntity user = (UserEntity) em.createNativeQuery(
                "SELECT * FROM users WHERE id = :id",
                UserEntity.class
            )
            .setParameter("id", id)
            .getSingleResult();

            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> findByUsername(String username) {
        try {
            UserEntity user = (UserEntity) em.createNativeQuery(
                "SELECT * FROM users WHERE username = :username",
                UserEntity.class
            )
            .setParameter("username", username)
            .getSingleResult();

            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();        
        }
    }

    @SuppressWarnings("unchecked")
    public List<UserEntity> findAll() {
        return em.createNativeQuery(
            "SELECT * FROM users ORDER BY created_at DESC",
            UserEntity.class
        ).getResultList();
    }


}
