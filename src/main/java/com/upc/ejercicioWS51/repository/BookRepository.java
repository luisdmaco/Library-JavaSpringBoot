package com.upc.ejercicioWS51.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.upc.ejercicioWS51.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findByEditorial(String editorial);
    boolean existsByTitleAndEditorial(String title, String editorial);

}
