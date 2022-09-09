package com.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String category;

    @Column
    private LocalDateTime date = LocalDateTime.now();
    @Column
    @NotBlank
    private String description;

    @Column
    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    @OrderColumn
    private String[] ingredients;

    @Column
    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    @OrderColumn
    private String[] directions;

    @Column
    @JsonIgnore
    private String author;
}