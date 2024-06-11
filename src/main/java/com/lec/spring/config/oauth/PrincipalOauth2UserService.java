package com.lec.spring.config.oauth;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.config.oauth.provider.FacebookUerInfo;
import com.lec.spring.config.oauth.provider.GoogleUserInfo;
import com.lec.spring.config.oauth.provider.NaverUserInfo;
import com.lec.spring.config.oauth.provider.OAuth2UserInfo;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * OAuth2UserService<OAuth2UserRequest, OAuth2User>(I)
 *  └─ DefaultOAuth2UserService
 */

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 인증후 '후처리' 를 여기서 작성.

    @Autowired
    private UserService userService;    //회원조회, 가입 시키기 위해

//    @Autowired
//    private PasswordEncoder passwordEncoder;    // 회원 가입 시 PW인코딩

    @Value("${app.oauth2.password}")
    private String oauth2Password;  //OAuth2 회원 가입시 기본 pw



    //SecurityConfig에 있는 OAuth2 인증 직후에 호출된다.
    //provider 로부터 받은 userRequest 데이터에 대한 후처리 진행.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2UserService.loadUser() 호출");

        OAuth2User oAuth2User = super.loadUser(userRequest);    // 사용자 프로필 정보 가져온다.
        // 어떠한 정보가 넘어오는지 확인
        System.out.println("""
         ClientRegistration: %s
         RegistrationId: %s
         AccessToken: %s
         OAuth2User Attributes : %s
       """.formatted(
               userRequest.getClientRegistration()  // 리턴값이 cloientRegistration 이다.
                , userRequest.getClientRegistration().getRegistrationId()   // id?
                ,userRequest.getAccessToken().getTokenValue()   //access token
                ,oAuth2User.getAttributes() //Map<String, Object> <- 사용자 프로필 정보
        ));

        // 후처리: 회원가입
        String provider = userRequest.getClientRegistration().getRegistrationId();    // ex: "google"

        OAuth2UserInfo oAuth2UserInfo = switch (provider.toLowerCase()) {
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            //TODO
            case "facebook" -> new FacebookUerInfo(oAuth2User.getAttributes());
            case"naver" -> new NaverUserInfo(oAuth2User.getAttributes());
            default -> null;
        };

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+ "_" + providerId;   // ex) "google_xxxxx"요런 식으로 들어감
        String password = oauth2Password;
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        // 회원가입 진행하기 전에
        // 이미 가입한 회원인지 ,혹은 비가입자인지 체크
        User user = userService.findByUsername(username);
        if (user == null) { //미가입자인 경우, 회원 가입 진행
            User newUser = User.builder()
                    .username(username)
                    .name(name)
                    .email(email)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .build();


            int cnt = userService.register(newUser);    // 회원가입!
            if (cnt > 0) {
                System.out.println("[OAuth2 인증. 회원가입이 되었습니다.]");
                user = userService.findByUsername(username);    // 위에 있지 않은 regDate와 같은 정보들을 받기 위해 받아와야한다.
            } else {
                System.out.println("[OAuth2 인증. 회원가입 실패]");
            }

        } else {
            System.out.println("[OAuth2 인증. 이미 가입된 회원입니다.]");
        }

        PrincipalDetails principalDetails = new PrincipalDetails(user,oAuth2User.getAttributes());
        principalDetails.setUserService(userService);
        return principalDetails;
    }

}
