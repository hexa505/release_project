package com.project.release.domain.album;

import lombok.Getter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Integer commentId;

    //예약어 조심 ~
    private Integer orders;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*
     * 자기 참조 일케 쓰는거 맞는지 몰겠넹 데베에 잘 표가 안남;
     * */

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
