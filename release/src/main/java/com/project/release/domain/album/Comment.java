package com.project.release.domain.album;

import com.project.release.domain.BaseTimeEntity;
import com.project.release.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private String content;

    // char(1)는
    // 데이터 정합성맞추려면 validation어쩌고어쩌고,, 해서 enum으로 넣음
    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted")
    private Check isDeleted; // Y/N

    @Builder
    public Comment(User user, Album album, Comment parent, String content, Check isDeleted) {
        this.user = user;
        this.album = album;
        this.parent = parent;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public void changeDeletedStatus(Check check) {
        this.isDeleted = check;
    }
}
