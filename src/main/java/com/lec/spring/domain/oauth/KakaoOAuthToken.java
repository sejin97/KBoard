package com.lec.spring.domain.oauth;

import lombok.Data;

//카카오 토큰을 받을 도메인
@Data
public class KakaoOAuthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}
