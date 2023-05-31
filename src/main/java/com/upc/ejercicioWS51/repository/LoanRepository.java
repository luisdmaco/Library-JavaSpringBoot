package com.upc.ejercicioWS51.repository;

import com.upc.ejercicioWS51.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByCodeStudent(String codeStudent);
    boolean existsByCodeStudentAndBook(String codeStudent, Long bookId, boolean bookLoan);
    List<Loan> findByCodeStudent(String codeStudent);
}
