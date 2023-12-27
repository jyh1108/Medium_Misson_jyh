package com.ll.medium.domain.writing;

import com.ll.medium.domain.DataNotFoundException;
import com.ll.medium.domain.member.MemberEntity;
import com.ll.medium.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WritingService {
    private final WritingRepository writingRepository;
    private final MemberService memberService;

    public Writing getWriting(Integer id) {
        Optional<Writing> writing = this.writingRepository.findById(id);
        if (writing.isPresent()) {
            return writing.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
    public void create(String subject, String content, MemberEntity member, boolean isPublished, boolean isPaid) {
        Writing q = new Writing();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(member);
        q.setPaid(isPaid);
        q.setPublished(isPublished);
        q.setCreateDate(LocalDateTime.now());
        this.writingRepository.save(q);
    }

    public Page<Writing> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
        return this.writingRepository.findAll(pageable);
    }

    public void delete(Writing writing) {
        this.writingRepository.delete(writing);
    }
    public void modify(Writing writing, String subject, String content, boolean isPublished) {
        writing.setSubject(subject);
        writing.setContent(content);
        writing.setPublished(isPublished);
        writing.setModifyDate(LocalDateTime.now());
        this.writingRepository.save(writing);
    }

    public Page<Writing> getMyList(String username, int page) {
        MemberEntity author = memberService.getUser(username);
        Pageable pageable = PageRequest.of(page, 30); // 10은 한 페이지에 보여줄 항목의 수입니다.
        return writingRepository.findByAuthor(author, pageable);
    }


    public Page<Writing> findByAuthor(String username, int page) {
        MemberEntity author = memberService.getUser(username);
        Pageable pageable = PageRequest.of(page, 30, Sort.by(Sort.Direction.DESC, "createDate"));
        return writingRepository.findByAuthor(author, pageable);
    }
}
