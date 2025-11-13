package org.example.res;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Reader;
import org.hibernate.Session;

import java.util.List;

@Path("/readers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReaderResource {

    @POST
    public Response createReader(Reader reader) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(reader);
            session.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(reader).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getReader(@PathParam("id") Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Reader reader = session.get(Reader.class, id);
            return reader != null
                    ? Response.ok(reader).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public List<Reader> getAllReaders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reader", Reader.class).list();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateReader(@PathParam("id") Long id, Reader updated) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Reader existing = session.get(Reader.class, id);
            if(existing == null) return Response.status(404).build();

            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());

            session.merge(existing);
            session.getTransaction().commit();
            return Response.ok(existing).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteReader(@PathParam("id") Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Reader reader = session.get(Reader.class, id);
            if(reader != null) session.remove(reader);
            session.getTransaction().commit();
            return Response.noContent().build();
        }
    }
}
