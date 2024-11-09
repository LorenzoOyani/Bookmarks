package org.example.bookmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bookmarks")
class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String url;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false)
    private Instant updatedAt;



    @PreUpdate
    @PrePersist
    public void updateTimeStamp(){
        this.updatedAt = Instant.now(); //PRE-UPDATE
        if(this.createdAt == null){ // PRE-PERSIST
            this.createdAt = Instant.now();
        }
    }
}
