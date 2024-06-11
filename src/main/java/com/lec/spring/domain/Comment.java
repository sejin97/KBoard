package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    private Long id;    //pk

    @ToString.Exclude
    private User user;  //댓글 작성자 (FK)

    @JsonIgnore
    private Long post_id;   // 어느 글의 댓글인지

    private String content; // 댓글 내용

    // java.time.* 객체 변환을 위한 annotation
    @JsonDeserialize (using = LocalDateTimeDeserializer.class)//1
    @JsonSerialize(using = LocalDateTimeSerializer.class)//2        1,2,3을 다 해야 내가 원하는 포맷으로 response 할 수 있다.
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/seoul")//3
    @JsonProperty("regdate")    // 변환하고자 하는 형태 지정 가능
    private LocalDateTime regDate;  // 날짜
}
