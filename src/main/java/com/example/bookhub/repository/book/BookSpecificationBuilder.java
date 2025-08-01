package com.example.bookhub.repository.book;

import com.example.bookhub.dto.book.BookSearchParameters;
import com.example.bookhub.model.Book;
import com.example.bookhub.repository.SpecificationBuilder;
import com.example.bookhub.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String[] SEARCH_KEYS = {"title", "author", "isbn", "categories"};

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        String[][] allParams = {
                bookSearchParameters.titles(),
                bookSearchParameters.authors(),
                bookSearchParameters.isbn(),
                bookSearchParameters.categories()
        };

        Specification<Book> spec = null;

        for (int i = 0; i < SEARCH_KEYS.length; i++) {
            String[] values = allParams[i];
            if (values != null && values.length > 0) {
                Specification<Book> part = specificationProviderManager
                        .getSpecificationProvider(SEARCH_KEYS[i])
                        .getSpecification(values);
                spec = (spec == null) ? part : spec.and(part);
            }
        }

        return spec;
    }
}
