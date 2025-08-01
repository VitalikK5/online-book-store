package com.example.bookhub.repository.book.spec;

import com.example.bookhub.model.Book;
import com.example.bookhub.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSpecificationKeys.CATEGORY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(BookSpecificationKeys.CATEGORY).in(Arrays.asList(params));
    }
}
