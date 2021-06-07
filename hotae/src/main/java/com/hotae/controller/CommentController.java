package com.hotae.controller;

import com.hotae.adaptor.GsonLocalDateTimeAdapter;
import com.hotae.domain.CommentDTO;
import com.hotae.service.CommentService;
import com.hotae.service.CommentServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//뷰가아닌 데이터자체를 반환
@RestController
public class CommentController {

    @Autowired
    private CommentService commentServiceImpl;

    /**
     * comments     등록  @PostMapping
     * comments/idx 수정  @PatchMapping
     */
//    1. 댓글 등록 & 수정
    @RequestMapping(value = { "/comments", "/comments/{idx}" }, method = { RequestMethod.POST, RequestMethod.PATCH })
    public JsonObject registerComment(@PathVariable(value = "idx", required = false) Long idx, @RequestBody final CommentDTO params) {

        JsonObject jsonObj = new JsonObject();

        try {
            /**
             * updateComment(view.html)함수(printComment()함수->openModal()->updateComment함수)는
             * 댓글번호를 params에 포함해서 전송하기 때문에 댓글번호 별도저장 안해도됨.
            */

            boolean isRegistered = commentServiceImpl.registerComment(params);
//            메서드실행결과를 result라는 이름으로 JSON객체에 추가해서 리턴
            jsonObj.addProperty("result", isRegistered);

        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
        }

        return jsonObj;
    }

//    2. 댓글 목록 조회
    @GetMapping(value = "/comments/{boardIdx}")
    public JsonObject getCommentList(@PathVariable("boardIdx") Long boardIdx, @ModelAttribute("params") CommentDTO params) {

        JsonObject jsonObj = new JsonObject();

        List commentList = commentServiceImpl.getCommentList(params);
//        댓글이 한개이상 있을 때
        if (CollectionUtils.isEmpty(commentList) == false) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
//            Json객체에 commentList라는 프로퍼티로 추가해서 리턴
            JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
            jsonObj.add("commentList", jsonArr);
        }

//        JsonArray 객체 자체를 리턴해도 되지만, JSON 객체에 담는 형태로 처리하면 JSON 객체에 여러 가지 타입의 데이터를 추가할 수 있기 때문
        return jsonObj;
    }

//    3. 댓글삭제
    @DeleteMapping(value = "/comments/{idx}")
    public JsonObject deleteComment(@PathVariable("idx") final Long idx) {

        JsonObject jsonObj = new JsonObject();

        try {
            boolean isDeleted = commentServiceImpl.deleteComment(idx);
            jsonObj.addProperty("result", isDeleted);
    
        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
        }

        return jsonObj;
    }


}
