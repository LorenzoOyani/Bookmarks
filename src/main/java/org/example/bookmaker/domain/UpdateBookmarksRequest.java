package org.example.bookmaker.domain;

import lombok.Getter;
import lombok.Setter;

public record UpdateBookmarksRequest(long id, String title, String url) {

}
