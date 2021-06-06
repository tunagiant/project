package com.hotae.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDTO extends CommonDTO {

    /**
     * changeYn     파일변경(추가,삭제,변경)여부
     * fileIdxs     파일인덱스(추가,삭제,변경 전 기존에 등록되어있던 파일번호(PK) 리스트)
     */
    private Long idx;
    private String title;
    private String content;
    private String writer;
    private int viewCnt;
    private String noticeYn;
    private String secretYn;
    private String changeYn;
    private List<Long> fileIdxs;

}
