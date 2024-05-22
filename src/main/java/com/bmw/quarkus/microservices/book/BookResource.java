package com.bmw.quarkus.microservices.book;

import io.smallrye.faulttolerance.FaultToleranceBinding;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

@Path("/api/books")
@Tag(name = "Book Rest API")
public class BookResource {

    @Inject
    Logger log;

    @Inject
    @RestClient
    NumberProxy numberProxy;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(summary = "Create a new book",
            description = "Create a new book with the given title, author, year of publication and genre")
    @Retry(maxRetries = 3, delay = 4000)
    @Fallback(fallbackMethod = "fallBackcreateBook")
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
                .isbn13(numberProxy.generateIsbnNumbers().isbn13)
                .creationDate(Instant.now())
                .build();
        log.info("Book has been created: " + book);
        return Response.status(201).entity(book).build();
    }

    public Response fallBackcreateBook(
            @FormParam("title") String title,
            @FormParam("author") String author,
            @FormParam("year") int yearOfPublication,
            @FormParam("genre") String genre) throws FileNotFoundException {


        var book = Book.builder()
                .title(title)
                .author(author)
                .yearOfPublication(yearOfPublication)
                .genre(genre)
                .isbn13("Will Set Later")
                .creationDate(Instant.now())
                .build();
        saveBookOndisk(book);
        log.warn("Number Service not available, fallback method will be used -  book: " + book);
        return Response.status(206).entity(book).build();
    }

    private void saveBookOndisk(Book book) throws FileNotFoundException {

        String bookJson = JsonbBuilder.create().toJson(book);
        try(PrintWriter out = new PrintWriter("book-" + Instant.now().toEpochMilli() + ".json")){
            out.println(bookJson);
        }
        log.info("Book has been saved on disk: " + book);
    }
}
