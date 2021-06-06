package com.hotae.mapper;

import com.hotae.domain.BoardDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMapper {

    int insertBoard(BoardDTO params);

    BoardDTO selectBoardDetail(Long idx);

    int updateBoard(BoardDTO params);

    int deleteBoard(Long idx);

    List<BoardDTO> selectBoardList(BoardDTO boardDTO);

//    페이징처리시 사용
    int selectBoardTotalCount(BoardDTO boardDTO);
}
