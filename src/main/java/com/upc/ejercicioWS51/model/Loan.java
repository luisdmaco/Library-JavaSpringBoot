package com.upc.ejercicioWS51.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code_student", nullable = false, length = 10)
    private String codeStudent;
    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;
    @Column(name = "devolution_date", nullable = false)
    private LocalDate devolutionDate;
    @Column(name = "book_loan", nullable = false)
    private boolean bookLoan;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false
            , foreignKey = @ForeignKey(name = "FK_BOOK_ID"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Book book;
}
