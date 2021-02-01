package com.project.release.domain.album;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    private String tag;

    @OneToMany(mappedBy = "tag")
    private List<PhotoTag> photoTags = new ArrayList<>();

}
