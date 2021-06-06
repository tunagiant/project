package com.hotae.util;


import com.hotae.domain.AttachDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*파일 업로드*/

@Component
public class FileUtils {

//    오늘날짜
    private final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

//    업로드경로
//    윈도우 환경에서 5월 28일을 기준으로 "C:\develop\ upload\210528"과 같은 패턴의 디렉터리(폴더)가 생성됨
    private final String uploadPath = Paths.get("C:", "develop", "upload", today).toString();

    /**
     * 서버에 생성할 파일명을 처리할 랜덤 문자열 반환
     * @return 랜덤 문자열
     */
    private final String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 서버에 첨부 파일을 생성 & 업로드 파일 목록 리턴
     * files                파일 Array
     * boardIdx             게시글 번호
     * attachList(return값) 업로드 파일 목록
     */
    public List<AttachDTO> uploadFiles(MultipartFile[] files, Long boardIdx) {

        List<AttachDTO> attachList = new ArrayList<>();

        File dir = new File(uploadPath);
        if (dir.exists() == false) {
            //mkdir != mkdirs
            dir.mkdirs();
        }

        for (MultipartFile file : files) {
//            기존에 등록되어있던 파일(size=0)일 경우 넘어감. 새로등록되는 파일은 size > 0
            if (file.getSize() < 1) {
                continue;
            }
            try {
                final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                final String saveName = getRandomString() + "." + extension;
                File target = new File(uploadPath, saveName);

                file.transferTo(target);

//                파일정보저장
                AttachDTO attach = new AttachDTO();
                attach.setBoardIdx(boardIdx);
                attach.setOriginalName(file.getOriginalFilename());
                attach.setSaveName(saveName);
                attach.setSize(file.getSize());

                attachList.add(attach);

            } catch (IOException e) {
                throw new RuntimeException("[" + file.getOriginalFilename() + "] 저장실패");

            } catch (Exception e) {
                throw new RuntimeException("[" + file.getOriginalFilename() + "] 저장실패");
            }
        }

        return attachList;
    }

}
