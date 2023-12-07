package com.ll.medium.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public MemberEntity create(String username, String password) {
        MemberEntity member = new MemberEntity();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(member);
        return member;
    }
}