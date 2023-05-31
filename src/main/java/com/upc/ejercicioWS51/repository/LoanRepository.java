package com.upc.ejercicioWS51.repository;

import com.upc.ejercicioWS51.model.Book;
import com.upc.ejercicioWS51.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByCodeStudentAndBookAndBookLoan(String codeStudent, Book book, boolean bookLoan);
    boolean existsByCodeStudent(String codeStudent);
    List<Loan> findByCodeStudent(String codeStudent);
}