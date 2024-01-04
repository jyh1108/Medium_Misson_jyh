package com.ll.medium.global.init;

import com.ll.medium.domain.member.MemberEntity;
import com.ll.medium.domain.member.MemberRepository;
import com.ll.medium.domain.member.MemberService;
import com.ll.medium.domain.writing.WritingRepository;
import com.ll.medium.domain.writing.WritingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final WritingService writingService;
    private final WritingRepository WritingRepository;
    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            for (int i = 1; i <= 100; i++) {
                String username = "user" + i;
                String password = "a123";
                MemberEntity member = memberService.create(username, password);
                member.setPaid(true);
                memberRepository.save(member);

                String subject = "제목" +i;
                String content = "내용" +i;

                writingService.create(subject,content,member,true,true);
            }
        };
    }
}