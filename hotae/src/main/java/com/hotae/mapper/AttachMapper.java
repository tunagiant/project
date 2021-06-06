package com.hotae.mapper;

import com.hotae.domain.AttachDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachMapper {

    int insertAttach(List<AttachDTO> attachList);

//    상세조회:업로드시에는 필요없지만 다운로드시 필요
    AttachDTO selectAttachDetail(Long idx);

//    게시글,댓글과 마찬가지로 레코드에서 삭제하진않고 delete_YN의 값을 바꾸는 식
    int deleteAttach(Long boardIdx);

//    삭제취소
    int undeleteAttach(List<Long> idxs);

    List<AttachDTO> selectAttachList(Long boardIdx);

    int selectAttachTotalCount(Long boardIdx);

}
