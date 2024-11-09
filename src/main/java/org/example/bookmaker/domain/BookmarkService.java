package org.example.bookmaker.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookmaker.exception.BookNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkDTO create(BookmarkRequest bookmark) {
        Bookmark bookmarks = new Bookmark(); // entity object
        bookmarks.setTitle(bookmark.getTitle());
        bookmarks.setUrl(bookmark.getUrl());
        bookmarks.setCreatedAt(Instant.now());
        return BookmarkDTO.from(bookmarkRepository.save(bookmarks)); // save to database
    }

    public PagedResult<BookmarkDTO> findBookmarks(FindBookmarkQuery query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int pageNo = query.pageNo() > 0 ? query.pageNo() - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, query.pageSize(), sort);

        Page<BookmarkDTO> page = bookmarkRepository.findBookmarks(pageable);

        return new PagedResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()

        );

    }

    public void updateBookmarks(UpdateBookmarksRequest request) {
        Bookmark bookmark = bookmarkRepository.findById(request.id())
                .orElseThrow(() -> BookNotFoundException.of(request.id()));
        bookmark.setTitle(request.title());
        bookmark.setUrl(request.url());
        bookmark.setCreatedAt(Instant.now());
        bookmarkRepository.save(bookmark);
    }

    Optional<BookmarkDTO> findById(Long id) {
        return bookmarkRepository.findBookmarkById(id);
    }

    void delete(Long id) {
        Bookmark bookmarks = bookmarkRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException.of(id));

        bookmarkRepository.delete(bookmarks);
    }

}
