package com.upc.ejercicioWS51.controller;

import com.upc.ejercicioWS51.exception.ValidationException;
import com.upc.ejercicioWS51.model.Book;
import com.upc.ejercicioWS51.model.Loan;
import com.upc.ejercicioWS51.repository.BookRepository;
import com.upc.ejercicioWS51.repository.LoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

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

    //EndPoint: http://localhost:8080/api/library/v1/loans/filterByCodeStudent
    //Method: GET
    @Transactional(readOnly = true)
    @RequestMapping("/loans/filterByCodeStudent")
    public ResponseEntity<List<Loan>> getLoansByCodeStudent(@RequestParam(name="codeStudent") String codeStudent){
        return new ResponseEntity<List<Loan>>(loanRepository.findByCodeStudent(codeStudent), HttpStatus.OK);
    }

    //EndPoint: http://localhost:8080/api/library/v1/books/1/loans
    //Method: GET
    @Transactional(readOnly = true)
    @RequestMapping("books/{id}/loans")
    public ResponseEntity<Loan> createLoan(@RequestParam(name = "id") long bookId,
                                           @RequestBody Loan loan) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceAccessException("No se encontro el libro con id: " + bookId));
        existsByCodeStudentAndBookAndBookLoan(loan, book);
        validateLoan(loan);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setBookLoan(true);
        return new ResponseEntity<>(loanRepository.save(loan), HttpStatus.CREATED);
    }

    private void validateLoan(Loan loan) {
        if (loan.getCodeStudent() == null || loan.getCodeStudent().trim().isEmpty()) {
            throw new ValidationException("El codigo del estudiante debe ser obligatorio.");
        }
        if (loan.getCodeStudent().length() < 10) {
            throw new ValidationException("El codigo del estudiante no debe exceder los 10 caracteres.");
        }
    }

    private void existsByCodeStudentAndBookAndBookLoan(Loan loan, Book book) {
        if (loanRepository.existsByCodeStudentAndBookAndBookLoan(loan.getCodeStudent(), book, true)) {
            throw new ValidationException("El prestamo del libro "+ book.getTitle() +
                    " no existe porque ya fue prestado por el alumno "+ loan.getCodeStudent()
            );
        }
    }
}
