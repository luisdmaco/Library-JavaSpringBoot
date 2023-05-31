package com.upc.ejercicioWS51.Service.impl;

import com.upc.ejercicioWS51.Service.BookService;
import com.upc.ejercicioWS51.model.Book;
import com.upc.ejercicioWS51.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
}
