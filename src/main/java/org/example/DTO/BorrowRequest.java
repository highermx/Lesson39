package org.example.DTO;

public class BorrowRequest {
    private Long bookId;
    private Long readerId;

    public Long getBookId() {
        return bookId;
    }
    public Long getReaderId() {
        return readerId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }
}
