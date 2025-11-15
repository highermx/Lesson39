package org.example;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    private String status;

    public void setBook(Book book) {
        this.book = book;
        book.getBorrowings().add(this);
    }

    public void setBorrowDate(LocalDate date) {
        this.borrowDate = date;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        if(returnDate != null) {
            this.status = "returned";
        }
    }
    public Reader getReader() {
        return reader;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
}

