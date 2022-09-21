package com.recipes.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username")
    @NotBlank
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    @Column
    @NotBlank
    @Size(min = 8)
    private String password;

    @NotEmpty
    @Column
    private String rolesAndAuthorities = "USER";

}
