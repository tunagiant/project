package com.hotae.domain;

import com.hotae.paging.Criteria;
import com.hotae.paging.PaginationInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommonDTO extends Criteria {

    /**
     * pagnationInfo    페이징정보
     * deleteYn         삭제여부
     */
    private PaginationInfo paginationInfo;
    private String deleteYn;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
