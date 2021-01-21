package com.project.release.domain.album;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Integer tagId;

    private String tag;
}
