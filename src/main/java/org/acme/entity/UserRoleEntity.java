package org.acme.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleEntity.UserRoleId.class)
public class UserRoleEntity extends PanacheEntityBase {
    
    @Id
    @Column(name = "user_id")
    public Long userId;

    @Id
    @Column(name = "role_id")
    public Long roleId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    public RoleEntity role;

    @Column(name = "created_at", nullable = false, updatable = false)
    public OffsetDateTime createdAt;

    @Column(name = "created_by", length = 100)
    public String createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // Composite Key Class
    public static class UserRoleId implements Serializable {
        public Long userId;
        public Long roleId;

        public UserRoleId() {
        }

        public UserRoleId(Long userId, Long roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserRoleId that = (UserRoleId) o;
            return userId.equals(that.userId) && roleId.equals(that.roleId);
        }

        @Override
        public int hashCode() {
            return userId.hashCode() + roleId.hashCode();
        }
    }
}
