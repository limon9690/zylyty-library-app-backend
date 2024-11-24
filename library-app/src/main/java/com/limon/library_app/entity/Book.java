package com.limon.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title"}),
        @UniqueConstraint(columnNames = {"isbn"})
})
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 30, message = "Title cannot be empty")
    private String title;

    @NotNull
    @Size(min = 1, max = 20, message = "Author cannot be empty")
    private String author;

    private String edition;
    private String publisher;

    @NotNull
    private BigInteger isbn;

    private String genre;
    private Integer pageCount;
    private String language;
    private Integer publicationYear;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookHistory> borrowedRecords;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime addedAt;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
