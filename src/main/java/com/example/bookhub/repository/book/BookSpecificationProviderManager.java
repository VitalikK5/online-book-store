package com.example.bookhub.repository.book;

import com.example.bookhub.model.Book;
import com.example.bookhub.repository.SpecificationProvider;
import com.example.bookhub.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No SpecificationProvider found for key '" + key + "'"));
    }
}
