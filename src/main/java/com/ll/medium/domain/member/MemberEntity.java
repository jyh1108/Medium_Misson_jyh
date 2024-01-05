package com.ll.medium.domain.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // (unique = true)
    private String username;

    private String password;
    @Column
    private boolean Paid = false;

}
