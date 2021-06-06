package com.hotae.service;

import com.hotae.domain.AttachDTO;
import com.hotae.domain.BoardDTO;
import com.hotae.mapper.AttachMapper;
import com.hotae.mapper.BoardMapper;
import com.hotae.paging.PaginationInfo;
import com.hotae.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private FileUtils fileUtils;

//    파일등록X
    @Override
    public boolean registerBoard(BoardDTO params) {
        int queryResult = 0;

        if (params.getIdx() == null) {
            queryResult = boardMapper.insertBoard(params);
        } else {
            queryResult = boardMapper.updateBoard(params);
        }

//        파일 추가, 삭제, 변경 시 기존의 파일 모두 삭제처리(delete_yn을 'Y'처리)
        if ("Y".equals(params.getChangeYn())) {
            attachMapper.deleteAttach(params.getIdx());
//            게시글에 있던 기존파일이 유지되는경우.
//            ex)A,B,C 있는 게시글에서 B, C만 삭제시 A는 유지(위에선 다 삭제했으므로 여기서 다시복구). A의 delete_yn을 'N'처리
            if (CollectionUtils.isEmpty(params.getFileIdxs()) == false) {
                attachMapper.undeleteAttach(params.getFileIdxs());
            }
        }



        return (queryResult == 1) ? true : false;
    }

//    파일등록O
    @Override
    public boolean registerBoard(BoardDTO params, MultipartFile[] files) {
        int queryResult = 1;

        if (registerBoard(params) == false) {
            return false;
        }

        /* 글 새로 작성 시, params.getIdx() 는 null값임(지금). 나중에 쿼리 실행시 게시글번호가 생기게되어
        게시글등록은 상관없지만 파일도 등록시에는 게시글번호를 전달할 수 없음(지금 이 순간, 아직 쿼리를 통하지 않아서
        이 메서드상에서는 전달할 수 없음). Mybatis의 useGeneratedKeys, keyProperty속성 이용해서 해결 */
        List<AttachDTO> fileList = fileUtils.uploadFiles(files, params.getIdx());
        if (CollectionUtils.isEmpty(fileList) == false) {
            queryResult = attachMapper.insertAttach(fileList);
            if (queryResult < 1) {
                queryResult = 0;
            }
        }

        return (queryResult > 0);
    }

    @Override
    public BoardDTO getBoardDetail(Long idx) {
        return boardMapper.selectBoardDetail(idx);
    }

    @Override
    public boolean deleteBoard(Long idx) {
        int queryResult = 0;

        BoardDTO board = boardMapper.selectBoardDetail(idx);

        if (board != null && "N".equals(board.getDeleteYn())) {
            queryResult = boardMapper.deleteBoard(idx);
        }

        return (queryResult == 1) ? true : false;
    }

    @Override
    public List<BoardDTO> getBoardList(BoardDTO params) {
        List<BoardDTO> boardList = Collections.emptyList();

        int boardTotalCount = boardMapper.selectBoardTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(boardTotalCount);

        params.setPaginationInfo(paginationInfo);

        if (boardTotalCount > 0) {
            boardList = boardMapper.selectBoardList(params);
        }

        return boardList;
    }

//    게시글 수정 시 원래 업로드 되어있던 파일목록을 볼 때(게시글 수정폼) 사용됨
    @Override
    public List<AttachDTO> getAttachFileList(Long boardIdx) {

        int fileTotalCount = attachMapper.selectAttachTotalCount(boardIdx);
        if (fileTotalCount < 1) {
            return Collections.emptyList();
        }
        return attachMapper.selectAttachList(boardIdx);
    }

//    파일다운로드
    @Override
    public AttachDTO getAttachDetail(Long idx) {
        return attachMapper.selectAttachDetail(idx);
    }
}
