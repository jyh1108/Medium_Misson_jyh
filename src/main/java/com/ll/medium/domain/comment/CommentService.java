package com.ll.medium.domain.comment;

import com.ll.medium.domain.DataNotFoundException;
import com.ll.medium.domain.member.MemberEntity;
import com.ll.medium.domain.writing.Writing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;


    public Comment  create(Writing writing, String content, MemberEntity author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setWriting(writing);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

//    public void vote(Answer answer, SiteUser siteUser) {
//        answer.getVoter().add(siteUser);
//        this.answerRepository.save(answer);
//    }
}
