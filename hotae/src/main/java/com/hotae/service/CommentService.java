package com.hotae.service;

import com.hotae.domain.CommentDTO;

import java.util.List;

public interface CommentService {

    boolean registerComment(CommentDTO params);

    boolean deleteComment(Long idx);

    List getCommentList(CommentDTO params);

}
