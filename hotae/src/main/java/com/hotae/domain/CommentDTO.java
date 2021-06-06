package com.hotae.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO extends CommonDTO {

    /**
     * idx      댓글번호
     * boardIdx 게시글번호(BoardDTO에서는 idx)
     */
    private Long idx;
    private Long boardIdx;
    private String content;
    private String writer;
}
