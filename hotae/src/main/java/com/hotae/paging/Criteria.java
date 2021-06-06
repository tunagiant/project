package com.hotae.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class Criteria {

    //현재 페이지 번호
    private int currentPageNo;

    //페이지당 출력할 데이터 개수
    private int recordsPerPage;

    //화면 하단에 출력할 페이지사이즈(1~5, 1~10...)
    private int pageSize;

    //검색키워드     동적SQL시 사용
    private String searchKeyword;

    //검색유형
    private String searchType;

    public Criteria() {
        this.currentPageNo = 1;
        this.recordsPerPage = 10;
        this.pageSize = 10;
    }

    //MySQL에서 LIMIT구문 앞에서 사용    프로퍼티접근법. 임시사용. PaginationInfo의 firstRecordIndex, lastRecordIndex가 대체
//    public int getstartPage() {
//        return (currentPageNo - 1) * recordsPerPage;
//    }


    //Criteria클래스의 멤버변수들이 쿼리스트링형태로 반환됨.
    //UriComponents : 스프링에서 제공함. URI를 더욱 효율적으로 처리 가능
    //현재페이지번호, 데이터개수, 하단에 출력할 페이지 개수 등을 쿼리스트링 형태로 반환
    public String makeQueryString(int pageNo) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParam("currentPageNo", pageNo)
                .queryParam("recordsPerPage", recordsPerPage)
                .queryParam("pageSize", pageSize)
                .queryParam("searchType", searchType)
                .queryParam("searchKeyword", searchKeyword)
                .build()
                .encode();

        return uriComponents.toUriString();
    }



}
