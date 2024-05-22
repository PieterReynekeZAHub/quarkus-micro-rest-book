package com.bmw.quarkus.microservices.book;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.time.Instant;

@Path("/api/books")
@Tag(name = "Book Rest API")
public class BookResource {

    @Inject
    Logger log;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(summary = "Create a new book",
            description = "Create a new book with the given title, author, year of publication and genre")
    public Response createBook(
            @FormParam("title") String title,
            @FormParam("author") String author,
            @FormParam("year") int yearOfPublication,
            @FormParam("genre") String genre) {

        var book = Book.builder()
                .title(title)
                .author(author)
                .yearOfPublication(yearOfPublication)
                .genre(genre)
                .isbn13("will get from Number Microservice")
                .creationDate(Instant.now())
                .build();
        log.info("Book has been created: " + book);
return Response.status(201).entity(book).build();
    }
}
