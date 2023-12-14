package com.ll.medium.domain.writing;

import com.ll.medium.domain.member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WritingRepository extends JpaRepository<Writing, Integer> {
    Writing findBySubject(String subject);
    Page<Writing> findByAuthor(MemberEntity author, Pageable pageable);
    Writing findBySubjectAndContent(String subject, String content);
    List<Writing> findBySubjectLike(String subject);

    Page<Writing> findAll(Pageable pageable);
}
