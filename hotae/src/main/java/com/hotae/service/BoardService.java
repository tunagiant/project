package com.hotae.service;

import com.hotae.domain.AttachDTO;
import com.hotae.domain.BoardDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    boolean registerBoard(BoardDTO params);

    boolean registerBoard(BoardDTO params, MultipartFile[] files);

    BoardDTO getBoardDetail(Long idx);

    boolean deleteBoard(Long idx);

    List<BoardDTO> getBoardList(BoardDTO params);

    List<AttachDTO> getAttachFileList(Long boardIdx);

    AttachDTO getAttachDetail(Long idx);

}
