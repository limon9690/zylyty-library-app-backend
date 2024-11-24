package com.limon.library_app.controller;

import com.limon.library_app.entity.Book;
import com.limon.library_app.exception.UnauthorizedException;
import com.limon.library_app.payload.BookDetail;
import com.limon.library_app.payload.BookRequest;
import com.limon.library_app.payload.BookResponse;
import com.limon.library_app.service.BookService;
import com.limon.library_app.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final SessionService sessionService;
    @Value("${admin.api.key}")
    private String adminApiKey;

    @PostMapping
    public ResponseEntity<?> createBooks(@Valid @RequestBody BookRequest bookRequest, @RequestHeader("Authorization") String authorizationHeader) {
       String token = authorizationHeader.replace("Bearer ", "");
       if (!adminApiKey.equals(token)) {
          throw new UnauthorizedException("Invalid API Key!");
       }

            List<Book> bookDetails = bookService.createBooks(bookRequest);
            List<Object> response = bookDetails.stream()
                    .map(details -> new Object() {
                        public final String isbn = details.getIsbn().toString();
                        public final String addedAt = details.getAddedAt().toString();
                    }).collect(Collectors.toList());

            return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<BookResponse> listBooks(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @CookieValue(name = "session") String sessionCookie
    ) {
        if (sessionCookie == null || !sessionService.isValidSession(sessionCookie)) {
            throw new UnauthorizedException("Unauthorized!!");

        }

        BookResponse response = bookService.listBooks(page, size);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookDetail bookDetail, @RequestHeader("Authorization") String authorizationHeader, @PathVariable String isbn) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (!adminApiKey.equals(token)) {
            throw new UnauthorizedException("Invalid API Key!");
        }

        Book updatedBook =  bookService.updateBook(bookDetail, isbn);
        return ResponseEntity.status(200).body(
            new Object() {
                public final String isbn = updatedBook.getIsbn().toString();
                public final String updated_at = updatedBook.getUpdatedAt().toString();
            }
        );
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (!adminApiKey.equals(token)) {
            throw new UnauthorizedException("Invalid API Key!");
        }

        bookService.deleteBook(isbn);
        return ResponseEntity.status(204).body("");
    }

}
