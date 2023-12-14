package com.ll.medium.domain.comment;

import com.ll.medium.domain.member.MemberEntity;
import com.ll.medium.domain.writing.Writing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Writing writing;

    @ManyToOne
    private MemberEntity author;

    private LocalDateTime modifyDate;

//    @ManyToMany
//    Set<MemberEntity> voter;

}
