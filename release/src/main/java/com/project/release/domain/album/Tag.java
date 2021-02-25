package com.project.release.domain.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    private String tagName;

    /*
    @JsonIgnore
    @OneToMany(mappedBy = "tag")
    private List<AlbumTag> albumTags = new ArrayList<>();
     */

    @Builder
    public Tag(String tagName) {
        this.tagName = tagName;
    }

}
