package com.clvspk.photo.controller;

import com.clvspk.photo.config.Config;
import com.clvspk.photo.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private Config config;


    @PostMapping
    public List<String> upload(@RequestParam("imgFile") MultipartFile[] multipartFiles) throws IOException {
        if (multipartFiles == null || multipartFiles.length <= 0) {
            Result.fail("请求参数异常");
        }
        List<String> list = new ArrayList<>();
        //图片基础路径
        String basePath = getImgPath();
        for (MultipartFile multipartFile : multipartFiles) {
            String picName =
                    UUID.randomUUID().toString().replace("-", "")
                            + multipartFile.getOriginalFilename().substring(
                            multipartFile.getOriginalFilename().lastIndexOf("."),
                            multipartFile.getOriginalFilename().length()
                    );
            File file = new File(basePath + File.separator + picName);
            multipartFile.transferTo(file);
            list.add(getHttpUrl(file.getPath()));
        }
        return list;
    }

    private String getImgPath() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonth().getValue());
        String day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (month.length() < 2) {
            day = "0" + day;
        }
        File file = new File(
                config.getBasePath()
                        + year + File.separator
                        + month + File.separator
                        + day);
        if (!file.exists()) {
            boolean createFileSuccess = file.mkdirs();
            if (!createFileSuccess) {
                Result.fail("文件目录创建失败");
                System.out.println(file.getPath());
            }
        }
        return file.getPath();
    }

    private String getHttpUrl(String filePath) {
        return config.getLocalhost() + filePath.replace(config.getBasePath(), "").replace("\\", "/");
    }

}
