### UPDATE 2021.06.06
게시글 CRUD(HTML FORM), 댓글 CRUD(HTTP API), 페이징기능, 첨부파일기능, MAPPER
회원가입(스프링시큐리티)기능 구현예정

------
#### FRONT END
+ JAVASCRIPT
+ JQUERY
+ HTML5
+ CSS3


#### DBMS
+ MYSQL


#### BACKEND
+ JAVA
+ THYMELEAF


#### WEB SERVER
+ TOMCAT(SPRING BOOT)

------

#### **java
+ /adaptor
    + GsonLocalDateTimeAdapter		댓글 작성 시간

+ /configuration
    + DBConfiguraton			DBMS
    + MvcConfiguration			파일첨부(multipartResolver)

+ /constant
    + Method				댓글 REST API 처리

+ /controller
    + BoardController			게시글 컨트롤러
    + Commentcontroller		댓글 컨트롤러

+ /domain
    + AttachDTO			파일첨부 DTO
    + BoardDTO			게시글 DTO
    + CommentDTO			댓글 DTO
    + CommonDTO			DTO 공통기능

+ /mapper
    + AttachMapper			파일첨부 mapper
    + BoardMapper			게시글 mapper
    + CommentMapper			댓글 mapper

+ /paging
    + Criteria
    + PaginationInfo

+ /service
    + BoardService			게시글 서비스
    + CommentService			댓글 서비스

+ /util
    + FileUtils				파일IO처리
    + UiUtils				오류메세지처리




#### **resource
+ /mapper				mapper 바인딩

+ /static				라이브러리, 플러그인 처리

+ /templates
    + list.html				게시글 리스트
    + view.html				게시글 상세조회
    + write.html			게시글 작성
    + utils/message-redircet.html		오류메세지처리

------
#### Scheme
>게시글
````
CREATE TABLE tb_board (
    idx INT NOT NULL AUTO_INCREMENT COMMENT '번호 (PK)',
    title VARCHAR(100) NOT NULL COMMENT '제목',
    content VARCHAR(3000) NOT NULL COMMENT '내용',
    writer VARCHAR(20) NOT NULL COMMENT '작성자',
    view_cnt INT NOT NULL DEFAULT 0 COMMENT '조회 수',
    notice_yn ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '공지글 여부',
    secret_yn ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '비밀글 여부',
    delete_yn ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
    insert_time DATETIME NOT NULL DEFAULT NOW() COMMENT '등록일',
    update_time DATETIME NULL COMMENT '수정일',
    delete_time DATETIME NULL COMMENT '삭제일',
    PRIMARY KEY (idx)
)  COMMENT '게시판';

````
>댓글
````
CREATE TABLE tb_comment (
    idx INT NOT NULL AUTO_INCREMENT COMMENT '번호 (PK)',
    board_idx INT NOT NULL COMMENT '게시글 번호 (FK)',
    content VARCHAR(3000) NOT NULL COMMENT '내용',
    writer VARCHAR(20) NOT NULL COMMENT '작성자',
    delete_yn ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
    insert_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
    update_time DATETIME DEFAULT NULL COMMENT '수정일',
    delete_time DATETIME DEFAULT NULL COMMENT '삭제일',
    PRIMARY KEY (idx)
) COMMENT '댓글';

alter table tb_comment add constraint fk_comment_board_idx foreign key (board_idx) references tb_board(idx);
SELECT *
FROM information_schema.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'board';
````

>파일업로드
````
CREATE TABLE tb_attach (
    idx INT NOT NULL AUTO_INCREMENT COMMENT '파일 번호 (PK)',
    board_idx INT NOT NULL COMMENT '게시글 번호 (FK)',
    original_name VARCHAR(260) NOT NULL COMMENT '원본 파일명',
    save_name VARCHAR(40) NOT NULL COMMENT '저장 파일명',
    size INT NOT NULL COMMENT '파일 크기',
    delete_yn ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
    insert_time DATETIME NOT NULL DEFAULT NOW() COMMENT '등록일',
    delete_time DATETIME NULL COMMENT '삭제일',
    PRIMARY KEY (idx)
) comment '첨부 파일';

alter table tb_attach add constraint fk_attach_board_idx foreign key (board_idx) references tb_board(idx);

select *
from information_schema.TABLE_CONSTRAINTS
where TABLE_SCHEMA = 'board';
````
