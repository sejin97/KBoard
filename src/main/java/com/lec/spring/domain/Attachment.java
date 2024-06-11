package com.lec.spring.domain;

import com.lec.spring.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {
    private Long id;
    private Long post_id;   // 어느글의 첨부파일?(FK)

    private String sourcename;
    private String filename;    // 저장된 파일명(rename 된 파일명)

    private boolean isImage; //이미지 여부
}
