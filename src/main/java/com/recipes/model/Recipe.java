package com.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
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
    @Size(min = 1)
    @ElementCollection
    @OrderColumn
    @NotEmpty
    private String[] ingredients;

    @Column
    @Size(min = 1)
    @ElementCollection
    @OrderColumn
    @NotEmpty
    private String[] directions;

    @Column
    @JsonIgnore
    private String author;

    public Recipe(String name, String category, LocalDateTime date, String description, String[] ingredients,
                  String[] directions, String author) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.author = author;

    }
}
