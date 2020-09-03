package com.quotemedia.interview.quoteservice.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "USERS")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "user_name", nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String encryptedPassword;

//    @ManyToMany(cascade= { CascadeType.PERSIST }, fetch = FetchType.EAGER )
//    @JoinTable(name="users_roles",
//            joinColumns=@JoinColumn(name="users_id",referencedColumnName="id"),
//            inverseJoinColumns=@JoinColumn(name="roles_id",referencedColumnName="id"))
//    private Collection<RoleEntity> roles;
}