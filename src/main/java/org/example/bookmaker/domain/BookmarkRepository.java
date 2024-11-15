package org.example.bookmaker.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("""
               SELECT
                new org.example.bookmaker.domain.BookmarkDTO(b.id, b.title, b.url, b.createdAt)
               FROM Bookmark b
            """)
    Page<BookmarkDTO> findBookmarks(Pageable pageable);

    @Query(
            """
                    SELECT new org.example.bookmaker.domain.BookmarkDTO(b.id, b.title, b.url, b.createdAt)
                    FROM Bookmark b WHERE b.id = ?1
                    """
    )
    Optional<BookmarkDTO> findBookmarkById(Long id);
}
