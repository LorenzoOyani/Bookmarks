package org.example.bookmaker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.awt.print.Book;
import java.net.URI;
import java.time.Instant;

public class BookNotFoundException extends ErrorResponseException {
    public BookNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("Bookmarks with " + id + " not found"), null);
    }

    public static BookNotFoundException of(long id) {
        return new BookNotFoundException(id);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Bookmark not found");
        problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
        problemDetail.setProperty("errorCategory", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
