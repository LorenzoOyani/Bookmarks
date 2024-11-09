package org.example.bookmaker.domain;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;


public record CreateBookmarksRequest(String title, String url) {

}
