package com.project.release.domain.album;

import com.project.release.domain.user.User;
import lombok.Getter;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    //예약어 조심 ~
    private Long orders;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment parent;

    @OneToOne(mappedBy = "parent")
    private Comment child;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    // char(1)는
    // 데이터 정합성맞추려면 validation어쩌고어쩌고,, 해서 enum으로 넣음
    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted")
    private Check isDeleted; // Y/N
}
