package com.limon.library_app.payload;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookDetail {
    @NotNull
    @Size(min = 1, message = "Title cannot be empty")
    private String title;

    @NotNull
    @Size(min = 1, message = "Author cannot be empty")
    private String author;

    private String edition;
    private String publisher;

    private String isbn;

    private String genre;
    private Integer pageCount;
    private String language;
    private Integer publicationYear;
    private String addedAt;
    private String updatedAt;

}
