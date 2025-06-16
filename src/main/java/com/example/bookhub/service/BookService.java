package com.example.bookhub.service;

import com.example.bookhub.dto.BookDto;
import com.example.bookhub.dto.BookSearchParameters;
import com.example.bookhub.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
