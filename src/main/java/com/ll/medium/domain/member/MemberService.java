package com.ll.medium.domain.member;

import com.ll.medium.domain.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public MemberEntity getUser(String username) {
        Optional<MemberEntity> memberUser = this.userRepository.findByusername(username);
        if (memberUser.isPresent()) {
            return memberUser.get();
        } else {
            throw new DataNotFoundException("memberUser not found");
        }
    }
}