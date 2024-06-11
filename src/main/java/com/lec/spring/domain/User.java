package com.lec.spring.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username; //회원아이디
    @JsonIgnore // json으로 뽑을 때 뽑지말아라
    private String password; //회원비밀번호

    @ToString.Exclude // toString()에서 제외
    @JsonIgnore
    private String re_password; // 폼에서 비밀번호 확인용 . db에 저장 안됌

    private String name;    // 회원 이름
    private  String email;  // 이메일

    @JsonIgnore
    private LocalDateTime regDate;  // 날짜

    //OAuth2 Client
    private String provider;//카카오 인증을 받아 쓴느 것을
    private  String providerId; //  provider 라고 하는데 여기서 제공하는 id 값 (pk)
}
