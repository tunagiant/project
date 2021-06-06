package com.hotae.util;

import com.hotae.constant.Method;
import com.hotae.paging.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

/*UI(오류메세지, 페이징)*/

@Controller
public class UiUtils {

    /**
     * message       사용자에게 전달할 메세지
     * redirectUri   리다이렉트URI
     * method        HTTP요청메서드
     * params        뷰로 전달할 파라미터
     */
    public String showMessageWithRedirect(@RequestParam(required = false) String message,
                                          @RequestParam(required = false) String redirectUri,
                                          @RequestParam(required = false) Method method,
                                          @RequestParam(required = false) Map<String, Object> params, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("redirectUri", redirectUri);
        model.addAttribute("method", method);
        model.addAttribute("params", params);

        return "utils/message-redirect";
    }

    /**
     * currentPageNo    현재 페이지
     * recordsPerPage   페이지당 글 수
     * pageSize         페이지 사이즈
     * searchType       검색유형
     * searchKeyword    검색키워드
     */
    public Map getPagingParams(Criteria criteria) {

        Map params = new LinkedHashMap<>();
        params.put("currentPageNo", criteria.getCurrentPageNo());
        params.put("recordsPerPage", criteria.getRecordsPerPage());
        params.put("pageSize", criteria.getPageSize());
        params.put("searchType", criteria.getSearchType());
        params.put("searchKeyword", criteria.getSearchKeyword());

        return params;
    }




}

