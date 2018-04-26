package com.iqmsoft.api;

import org.springframework.stereotype.Component;

import com.iqmsoft.domain.Note;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Path("/notes")
public class NoteResource {

    private final List<Note> notes = new ArrayList<Note>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotes() {
        return Response.ok(notes).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Note note) {
        note.setId(idCounter.getAndIncrement());
        synchronized (notes) {
            notes.add(note);
        }
        return Response.ok(note).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        synchronized (notes) {
            Iterator<Note> iter = notes.iterator();
            while (iter.hasNext()) {
                Note note = iter.next();
                if (note.getId().toString().equals(id)) {
                    return Response.ok(note).build();
                }
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") final String id, Note note) {
        synchronized (notes) {
            Iterator<Note> iter = notes.iterator();
            while (iter.hasNext()) {
                Note existingNote = iter.next();
                if (existingNote.getId().toString().equals(id)) {
                    existingNote.setText(note.getText());
                    return Response.ok(note).build();
                }
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") final String id) {
        synchronized (notes) {
            Iterator<Note> iter = notes.iterator();
            while (iter.hasNext()) {
                Note note = iter.next();
                if (note.getId().toString().equals(id)) {
                    iter.remove();
                    return Response.ok(note).build();
                }
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
