package com.limon.library_app.repository;

import com.limon.library_app.entity.BookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookHistoryRepository extends JpaRepository<BookHistory, Long> {

    boolean existsByBookIdAndIsReturnedFalse(Long bookId);

    int countByUserIdAndIsReturnedFalse(Long userId);

    List<BookHistory> findAllByUserId(Long userId);

    Optional<BookHistory> findByUserIdAndBookIdAndIsReturnedFalse(Long userId, Long bookId);
}
