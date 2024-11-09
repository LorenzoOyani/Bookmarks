package org.example.bookmaker.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BookmarkRequest {
    private String title;
    private String url;

    public BookmarkRequest(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public BookmarkRequest() {
    }

    ;
}
