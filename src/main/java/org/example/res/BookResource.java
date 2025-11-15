package org.example.res;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Book;
import org.hibernate.Session;

import java.util.List;


@Path("/books")
public class BookResource {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(book);
            session.getTransaction().commit();
            return Response.status(201).entity(book).build();
        }
    }

    @GET
    @Path("/borrowed/{readerId}")
    public List<Book> getBorrowedBooks(@PathParam("readerId") Long readerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT b.book FROM BorrowedBook b WHERE b.reader.id = :readerId AND b.status = 'borrowed'",
                            Book.class)
                    .setParameter("readerId", readerId)
                    .list();
        }
    }
}
