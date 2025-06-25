package com.example.bookhub.repository;

import com.example.bookhub.dto.book.BookSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters bookSearchParameters);
}
