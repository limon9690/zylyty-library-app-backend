package com.limon.library_app.service;

import com.limon.library_app.entity.Book;
import com.limon.library_app.entity.BookHistory;
import com.limon.library_app.entity.User;
import com.limon.library_app.payload.BookHistoryDetail;
import com.limon.library_app.payload.BorrowBookRequest;
import com.limon.library_app.payload.BorrowBookResponse;
import com.limon.library_app.payload.BorrowedBooksResponse;
import com.limon.library_app.repository.BookHistoryRepository;
import com.limon.library_app.repository.BookRepository;
import com.limon.library_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookHistoryService {
    private final BookHistoryRepository bookHistoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest, String email) {
        Book book = bookRepository.findByIsbn(new BigInteger(borrowBookRequest.getIsbn()))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (bookHistoryRepository.existsByBookIdAndIsReturnedFalse(book.getId())) {
            throw new IllegalStateException("Book is already borrowed");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (bookHistoryRepository.countByUserIdAndIsReturnedFalse(user.getId()) >= 2) {
            throw new IllegalStateException("User has reached the borrowing limit of 2 books.");
        }

        BookHistory bookHistory = new BookHistory();
        bookHistory.setUser(user);
        bookHistory.setBook(book);
        bookHistory.setReturned(false);

        BookHistory savedHistory = bookHistoryRepository.save(bookHistory);

        return new BorrowBookResponse(borrowBookRequest.getIsbn(), bookHistory.getBorrowedAt().toString());
    }

    @Transactional
    public void returnBook(String isbn, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = bookRepository.findByIsbn(new BigInteger(isbn ))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        BookHistory bookHistory = bookHistoryRepository.findByUserIdAndBookIdAndIsReturnedFalse(user.getId(), book.getId())
                .orElseThrow(() -> new EntityNotFoundException("No record found"));

        bookHistory.setReturned(true);
        bookHistoryRepository.save(bookHistory);
    }


    public BorrowedBooksResponse getBorrowedBooks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<BookHistory> histories = bookHistoryRepository.findAllByUserId(user.getId());

        if (histories.isEmpty()) {
            return new BorrowedBooksResponse(new ArrayList<>());
        }

        List<BookHistoryDetail> historyDetails = histories.stream()
                .map(history -> new BookHistoryDetail(
                        history.getBook().getIsbn().toString(),
                        history.getBorrowedAt().toString(),
                        history != null && history.getReturnedAt() != null
                                ? history.getReturnedAt().toString()
                                : null
                ))
                .toList();

        BorrowedBooksResponse response = new BorrowedBooksResponse();
        response.setBooks(historyDetails);
        return response;
    }
}
