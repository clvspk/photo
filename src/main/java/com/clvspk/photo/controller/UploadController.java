package com.clvspk.photo.controller;

import com.clvspk.photo.config.Config;
import com.clvspk.photo.response.Result;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private Config config;

    private String basePath = null;

    private Long overTime = null;


    @PostMapping
    public List<String> upload(@RequestParam("imgFile") MultipartFile[] multipartFiles, HttpServletRequest request) throws IOException {
        if (multipartFiles == null || multipartFiles.length <= 0) {
            Result.fail("请求参数异常");
        }
        List<String> list = new ArrayList<>();
        String basePath = getBasePath();
        //图片基础路径
        for (MultipartFile multipartFile : multipartFiles) {
            if (isImg(multipartFile.getInputStream())){
                String picName =
                        UUID.randomUUID().toString().replace("-", "")
                                + multipartFile.getOriginalFilename().substring(
                                multipartFile.getOriginalFilename().lastIndexOf("."),
                                multipartFile.getOriginalFilename().length()
                        );
                File file = new File(basePath + File.separator + picName);
                multipartFile.transferTo(file);
                list.add(getHttpUrl(file.getPath()));
            }else {
                System.out.println(
                        String.format("Ip: %s , FileError: %s , Time: %s",
                                request.getRemoteAddr(),
                                multipartFile.getOriginalFilename(),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
        }
        return list;
    }

    private String getBasePath() {
        if (this.overTime == null || System.currentTimeMillis() > this.overTime) {
            synchronized (this) {
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
                this.basePath = file.getPath();
                this.overTime = Date.from(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant()).getTime();
            }
        }
        return this.basePath;
    }

    private String getHttpUrl(String filePath) {
        return config.getLocalhost() + filePath.replace(config.getBasePath(), "").replace("\\", "/");
    }

    private boolean isImg(InputStream inputStream){
        try {
            Image image = ImageIO.read(inputStream);
            return image != null;
        } catch(IOException ex) {
            return false;
        }
    }


}
