package com.gabrego.libraryrestapi.services;

import com.gabrego.libraryrestapi.domain.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createBook(String isbn, Book book);

    List<Book> findAll();

    Optional<Book> findOne(String isbn);
}