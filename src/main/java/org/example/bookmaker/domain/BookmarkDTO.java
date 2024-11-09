package org.example.bookmaker;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public record BookmarkDTO(
        Long id,
        String title,
        String url,
        Instant createdAt
) {
}
