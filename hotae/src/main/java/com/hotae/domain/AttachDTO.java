package com.hotae.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachDTO extends CommonDTO {

    /**
     * idx          파일번호(PK)
     * boardIdx     게시글번호(FK)
     */
    private Long idx;
    private Long boardIdx;
    private String originalName;
    private String saveName;
    private long size;
}
