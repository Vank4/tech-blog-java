package com.techblog.domain.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@IdClass(UserRoleId.class)
@Table(name = "user_roles")
public class UserRole {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false, nullable = false)
    private LocalDateTime assignedAt;
}