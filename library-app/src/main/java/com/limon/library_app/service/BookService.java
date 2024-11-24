package com.limon.library_app.service;

import com.limon.library_app.entity.Book;
import com.limon.library_app.payload.BookDetail;
import com.limon.library_app.payload.BookRequest;
import com.limon.library_app.payload.BookResponse;
import com.limon.library_app.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<Book> createBooks(BookRequest bookRequest) {
        List<BookDetail> books = bookRequest.getBooks();

        if (books.isEmpty()) {
            throw new IllegalArgumentException("No Content");
        }

//        List<Book> booksToSave = books.stream()
//                .map(details -> modelMapper.map(details, Book.class))
//                .toList();

        List<Book> booksToSave = books.stream()
                .map(details -> {
                    Book book = new Book();
                    book.setTitle(details.getTitle());
                    book.setAuthor(details.getAuthor());
                    book.setEdition(details.getEdition());
                    book.setPublisher(details.getPublisher());
                    book.setIsbn(new BigInteger(details.getIsbn()));
                    book.setGenre(details.getGenre());
                    book.setPageCount(details.getPageCount());
                    book.setLanguage(details.getLanguage());
                    book.setPublicationYear(details.getPublicationYear());
                    return book;
                })
                .collect(Collectors.toList());

        List<Book> savedBooks = bookRepository.saveAll(booksToSave);

        return savedBooks;
    }

    public BookResponse listBooks(int page, int size) {
        Sort sort = Sort.by("isbn").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> bookPage = bookRepository.findAll(pageable);

        List<Book> bookList = bookPage.toList();
        List<BookDetail> bookDetailList = bookList.stream()
                .map(book -> modelMapper.map(book, BookDetail.class))
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setBooks(bookDetailList);
        bookResponse.setTotal_pages(bookPage.getTotalPages());
        bookResponse.setPage(bookPage.getNumber());

        return bookResponse;
    }

    @Transactional
    public Book updateBook(@Valid BookDetail bookDetail, String isbn) {
        Book dbBook = bookRepository.findByIsbn(new BigInteger(isbn))
                .orElseThrow(() -> new EntityNotFoundException());


        dbBook.setAuthor(bookDetail.getAuthor());
        dbBook.setGenre(bookDetail.getGenre());
        dbBook.setPublisher(bookDetail.getPublisher());
        dbBook.setEdition(bookDetail.getEdition());
        dbBook.setPublicationYear(bookDetail.getPublicationYear());
        dbBook.setPageCount(bookDetail.getPageCount());
        dbBook.setLanguage(bookDetail.getLanguage());

        Book savedBook = bookRepository.save(dbBook);
        return savedBook;
    }

    @Transactional
    public void deleteBook(String isbn) {
        Book dbBook = bookRepository.findByIsbn(new BigInteger(isbn))
                .orElseThrow(() -> new EntityNotFoundException());
        bookRepository.deleteById(dbBook.getId());
    }
}
