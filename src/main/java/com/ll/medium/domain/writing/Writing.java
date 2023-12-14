package com.ll.medium.domain.writing;

import com.ll.medium.domain.comment.Comment;
import com.ll.medium.domain.member.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Writing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 200)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private MemberEntity author;

    @OneToMany(mappedBy = "writing", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    private LocalDateTime modifyDate;
    @Column
    private boolean isPublished = false;


}

