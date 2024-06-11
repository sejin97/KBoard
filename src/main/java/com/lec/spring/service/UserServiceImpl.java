package com.lec.spring.service;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AuthorityRepository;
import com.lec.spring.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder; //암호화를 위해

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public UserServiceImpl(SqlSession session){
        userRepository = session.getMapper(UserRepository.class);
        authorityRepository = session.getMapper(AuthorityRepository.class);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username.toUpperCase());
    }

    @Override
    public boolean isExist(String username) {
        User user = findByUsername(username.toUpperCase());
        return user != null; // 유저 이름의 존내 유무확인
    }

    @Override
    public int register(User user) {
        user.setUsername(user.getUsername().toUpperCase()); // DB 에는 username 을 대문자로 저장
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 암호화해서 저장
        userRepository.save(user); // 신규회원 저장, id값 받아옴

        //신규회원은 ROLE_MOMBER 권한을 기본적으로 부여하기
        Authority auth = authorityRepository.findByName("ROLE_MEMBER");

        authorityRepository.addAuthority(user.getId(), auth.getId());


        return 1;
    }

    @Override
    public List<Authority> selectAuthoritiesById(Long id) {
        User user = userRepository.findById(id);


        return authorityRepository.findByUser(user);
    }
}
