package com.upc.ejercicioWS51.controller;


import com.upc.ejercicioWS51.exception.ResourceNotFoundException;
import com.upc.ejercicioWS51.exception.ValidationException;
import com.upc.ejercicioWS51.model.Book;
import com.upc.ejercicioWS51.model.Loan;
import com.upc.ejercicioWS51.repository.LoanRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upc.ejercicioWS51.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/library/v1")
public class LoanController {

    private LoanRepository loanRepository;
    private BookRepository bookRepository;

    public LoanController(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    //EndPoint: http://lovalhost:8080/api/library/v1/loans/filterByCodeStudent
    //Method: GET
    @Transactional(readOnly = true)
    @RequestMapping("/loans/filterByCodeStudent")
    public ResponseEntity<List<Loan>> getLoansByCodeStudent(@RequestParam String codeStudent) {
        List<Loan> loans = loanRepository.findByCodeStudent(codeStudent);
        return ResponseEntity.ok(loans);
    }

    //EndPoint: http://lovalhost:8080/api/library/v1/books/1/loans
    //Method: Post
    @Transactional
    @RequestMapping("/books/{id}/loans")
    public ResponseEntity<Loan> createLoan(@PathVariable(value = "id") long bookId,
                                           @RequestBody Loan loan) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el libro con el id: " + bookId));
        existsByCodeStudentAndBookAndBookLoan(loan, book);
        validateLoan(loan);
        loan.setLoanDate(LocalDate.now());
        loan.setDevolutionDate(LocalDate.now().plusDays(3));
        loan.setBookLoan(true);
        return new ResponseEntity<Loan>(loanRepository.save(loan), HttpStatus.CREATED);
    }

    private void validateLoan(Loan loan) {
        if(loan.getCodeStudent() == null || loan.getCodeStudent().isEmpty()) {
            throw new RuntimeException("El código de estudiante es obligatorio");
        }
        if(loan.getCodeStudent().length() > 10) {
            throw new ValidationException("El código de estudiante no puede tener 10 caracteres");
        }
    }

    private void existsByCodeStudentAndBookAndBookLoan(Loan loan, Book book){
        if(loanRepository.existsByCodeStudentAndBookAndBookLoan(loan.getCodeStudent(), book, true)){
            throw new ValidationException("El prestamo del libro " + book.getTitle() + " no es posible porque ya fue prestado por el alumno"+ loan.getCodeStudent());
        }
    }
}