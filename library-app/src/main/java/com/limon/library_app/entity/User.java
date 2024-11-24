package com.limon.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 3, max = 10)
    private String username;

    @NotBlank
    private String password; // Store encrypted passwords

    @Email
    @NotBlank
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BookHistory> borrowedBooks;
}
