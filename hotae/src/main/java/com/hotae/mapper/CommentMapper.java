package com.hotae.mapper;

import com.hotae.domain.CommentDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {

    int insertComment(CommentDTO params);

//    댓글상세보기 : 삭제에 이용
    CommentDTO selectCommentDetail(Long idx);

    int updateComment(CommentDTO params);

    int deleteComment(Long idx);

    List selectCommentList(CommentDTO params);

    int selectCommentTotalCount(CommentDTO params);
}
