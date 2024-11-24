package com.limon.library_app.controller;

import com.limon.library_app.entity.BookHistory;
import com.limon.library_app.exception.UnauthorizedException;
import com.limon.library_app.payload.BorrowBookRequest;
import com.limon.library_app.payload.BorrowBookResponse;
import com.limon.library_app.payload.BorrowedBooksResponse;
import com.limon.library_app.service.BookHistoryService;
import com.limon.library_app.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class BookHistoryController {
    private final BookHistoryService bookHistoryService;
    private final SessionService sessionService;

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook
            (
                    @CookieValue(name = "session", required = false) String sessionCookie,
                    @RequestBody BorrowBookRequest borrowBookRequest
            ) {
        if (sessionCookie == null || !sessionService.isValidSession(sessionCookie)) {
            throw new UnauthorizedException("Unauthorized!!");

        }

        String email = sessionService.getEmailBySession(sessionCookie);
        BorrowBookResponse response = bookHistoryService.borrowBook(borrowBookRequest, email);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/return/{isbn}")
    public ResponseEntity<?> returnBook(
        @PathVariable String isbn,
        @CookieValue(name = "session", required = true) String sessionCookie
    ) {
        if (sessionCookie == null || !sessionService.isValidSession(sessionCookie)) {
            throw new UnauthorizedException("Unauthorized!!");
        }

        String email = sessionService.getEmailBySession(sessionCookie);

        bookHistoryService.returnBook(isbn, email);
        return ResponseEntity.status(204).body("");
    }

    @GetMapping("/borrowed")
    public ResponseEntity<BorrowedBooksResponse> getBorrowedBooks
            (
                    @CookieValue(name = "session", required = true) String sessionCookie
            ) {
        if (sessionCookie == null || !sessionService.isValidSession(sessionCookie)) {
            throw new UnauthorizedException("Unauthorized!!");
        }

        String email = sessionService.getEmailBySession(sessionCookie);
        BorrowedBooksResponse response = bookHistoryService.getBorrowedBooks(email);
        return ResponseEntity.status(200).body(response);
    }
}
