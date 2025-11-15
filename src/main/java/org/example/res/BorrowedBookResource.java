package org.example.res;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Book;
import org.example.BorrowedBook;
import org.example.DTO.BorrowRequest;
import org.example.Reader;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;


@Path("/borrowed-books")
@Produces(MediaType.APPLICATION_JSON)
public class BorrowedBookResource {

    @GET
    public List<BorrowedBook> getAllBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM BorrowedBook", BorrowedBook.class).list();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBorrowing(@PathParam("id") Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            BorrowedBook borrowing = session.get(BorrowedBook.class, id);
            return borrowing != null
                    ? Response.ok(borrowing).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createBorrowing(BorrowRequest request) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Book book = session.get(Book.class, request.getBookId());
            Reader reader = session.get(Reader.class, request.getReaderId());

            if(book == null || reader == null) {
                return Response.status(404).entity("Book or Reader not found").build();
            }

            BorrowedBook borrowing = new BorrowedBook();
            borrowing.setBook(book);
            borrowing.setReader(reader);
            borrowing.setBorrowDate(LocalDate.now());
            borrowing.setStatus("borrowed");

            session.persist(borrowing);
            session.getTransaction().commit();
            return Response.status(201).entity(borrowing).build();
        }
    }

    @PUT
    @Path("/{id}/return")
    public Response returnBook(@PathParam("id") Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            BorrowedBook borrowing = session.get(BorrowedBook.class, id);
            if(borrowing == null) return Response.status(404).build();

            borrowing.setReturnDate(LocalDate.now());
            borrowing.setStatus("returned");
            session.merge(borrowing);

            session.getTransaction().commit();
            return Response.ok(borrowing).build();
        }
    }
}
