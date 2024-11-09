package org.example.bookmaker.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class BookmakerController {

    private final BookmarkService bookmarkService;

    @PostMapping("/create")
    ResponseEntity<BookmarkDTO> create(@RequestBody @Validated CreateBookmarksRequest request) { // separation of concerns
        BookmarkRequest bookmarkRequest = new BookmarkRequest();
        bookmarkRequest.setUrl(request.url());
        bookmarkRequest.setTitle(request.title());
        BookmarkDTO bookmarkDTO = bookmarkService.create(bookmarkRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("api/v1/create/{id}")
                .buildAndExpand(bookmarkDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(bookmarkDTO);
    }

    @GetMapping("/bookmark")
    PagedResult<BookmarkDTO> findBookmarks(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize
    ) {
        FindBookmarkQuery query = new FindBookmarkQuery(pageNo, pageSize);
        return bookmarkService.findBookmarks(query);
    }

    @PutMapping("updateBookmark/{id}")
    void update(
            @PathVariable long id,
            @RequestBody @Validated UpdateBookmarks request) {
        UpdateBookmarksRequest request1 = new UpdateBookmarksRequest(id, request.getTitle(), request.getUrl());
        bookmarkService.updateBookmarks(request1);
    }

    @GetMapping("findBookmark/{id}")
    public ResponseEntity<BookmarkDTO> findBookmark(@PathVariable long id) {
        return bookmarkService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("delete/{id}")
    void delete(@PathVariable(name = "id") Long id){
        bookmarkService.delete(id);
    }
}
