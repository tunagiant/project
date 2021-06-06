package com.hotae.controller;

import com.hotae.constant.Method;
import com.hotae.domain.AttachDTO;
import com.hotae.domain.BoardDTO;
import com.hotae.service.BoardServiceImpl;
import com.hotae.util.UiUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController extends UiUtils {

    @Autowired
    private BoardServiceImpl boardService;

//    1. 등록 & 수정(GET)
    @GetMapping("/board/write.do")
    public String openBoardWrite(@ModelAttribute("params") BoardDTO params, @RequestParam(required = false) Long idx, Model model) {

        if (idx == null) {
//            신규등록
            model.addAttribute("board", new BoardDTO());
        } else {
//            수정
            BoardDTO board = boardService.getBoardDetail(idx);
            if (board == null || "Y".equals(board.getDeleteYn())) {
                return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
            }
            model.addAttribute("board", board);

//            게시글 수정 시 이미 업로드되어있던 파일
            List<AttachDTO> fileList = boardService.getAttachFileList(idx);
            model.addAttribute("fileList", fileList);
        }

        return "board/write";
    }

//    1-1. 등록 & 수정(POST)
    @PostMapping(value = "/board/register.do")
    public String registerBoard(@ModelAttribute("params")BoardDTO params, final MultipartFile[] files, Model model) {
//        페이징처리
        Map pagingParams = getPagingParams(params);

        try {
            boolean isRegistered = boardService.registerBoard(params, files);
            if (isRegistered == false) {
                return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
            }
        } catch (DataAccessException e) {
            return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);

        } catch (Exception e) {
            return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
        }

        return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
    }

//     2. 목록
    @GetMapping("/board/list.do")
    public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {

        List<BoardDTO> boardList = boardService.getBoardList(params);
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

//    3. 상세조회
    @GetMapping("/board/view.do")
    public String openBoardDetail(@ModelAttribute("params")BoardDTO params, @RequestParam(required = false) Long idx, Model model) {

        if (idx == null) {
            return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
        }

        BoardDTO board = boardService.getBoardDetail(idx);

        if (board == null || "Y".equals(board.getDeleteYn())) {
            return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
        }

//        첨부파일처리
        model.addAttribute("board", board);
        List<AttachDTO> fileList = boardService.getAttachFileList(idx);
        model.addAttribute("fileList", fileList);
        return "board/view";
    }

//    3-1. 파일다운로드
    @GetMapping("/board/download.do")
    public void downloadAttachFile(@RequestParam(value = "idx", required = false) final Long idx, Model model, HttpServletResponse response) {

        if (idx == null) throw new RuntimeException("올바르지 않은 접근입니다.");

        AttachDTO fileInfo = boardService.getAttachDetail(idx);

        if (fileInfo == null || "Y".equals(fileInfo.getDeleteYn())) throw new RuntimeException("파일 정보를 찾을 수 없습니다.");

        String uploadDate = fileInfo.getInsertTime().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uploadPath = Paths.get("C:", "develop", "upload", uploadDate).toString();

        String filename = fileInfo.getOriginalName();
        File file = new File(uploadPath, fileInfo.getSaveName());

        try {
//            FileUtils:커스텀클래스(com.hotae.util.FileUtils)가 아님, 아파치기존클래스
            byte[] data = FileUtils.readFileToByteArray(file);
//            다운로드될파일 설정
            response.setContentType("application/octet-stream");
            response.setContentLength(data.length);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(filename, "UTF-8") + "\";");

//            파일다운로드시작
            response.getOutputStream().write(data);
//            파일다운로드완료
            response.getOutputStream().flush();
//            버퍼정리
            response.getOutputStream().close();

        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드에 실패하였습니다.");

        } catch (Exception e) {
            throw new RuntimeException("시스템에 문제가 발생하였습니다.");
        }
    }

//    4. 삭제
    @PostMapping(value = "/board/delete.do")
    public String deleteBoard(@ModelAttribute("params")BoardDTO params, @RequestParam(required = false) Long idx, Model model) {

        if (idx == null) {
            return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
        }

        Map pagingParams = getPagingParams(params);

        try {
            boolean isDeleted = boardService.deleteBoard(idx);
            if (isDeleted == false) {
                return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
            }
        } catch (DataAccessException e) {
            return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);

        } catch (Exception e) {
            return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
        }

        return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
    }


}


