package com.lec.spring.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {
    private Long id;    // pk
    private String name; // 권한명 ex)"ROLE_MEMBER, ROLE_ADMIN
}
