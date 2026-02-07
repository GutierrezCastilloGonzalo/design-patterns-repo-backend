package com.academicrepo.back.academic_repo.users.infrastructure.entities;

import com.academicrepo.back.academic_repo.general.entities.repositoryEntities.BaseAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends BaseAbstractEntity {

    @Column(nullable = true, length = 100, unique = true, name = "user_name")
    private String userName;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 255, name = "password_hash")
    private String passwordHash;
}
