package com.clvspk.photo.controller;

import com.clvspk.photo.response.Result;
import com.clvspk.photo.utils.Assert;
import com.clvspk.photo.utils.PhotoLog;
import com.clvspk.photo.utils.PhotoUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private PhotoUtils photoUtils;


    /**
     * Upload Photo
     */
    @PostMapping
    public List<String> upload(@RequestParam("imgFile") MultipartFile[] multipartFiles, HttpServletRequest request) throws IOException {
        if (Assert.isNull(multipartFiles)) {
            Result.fail("Request Params Error");
        }

        // Result List
        List<String> list = new ArrayList<>();

        //Photo Server Storage Path
        String basePath = photoUtils.getBasePath();

        for (MultipartFile multipartFile : multipartFiles) {

            //is Photo
            if (PhotoUtils.isPhoto(multipartFile.getInputStream())) {

                // Create UUID Photo Name
                String picName = PhotoUtils.getPhotoName(multipartFile);

                //Create Photo File
                File file = new File(basePath + File.separator + picName);
                multipartFile.transferTo(file);

                //Create Http Addr
                String httpUrl = photoUtils.getHttpUrl(file.getPath());
                list.add(httpUrl);

                //Insert Log
                PhotoLog.success(request, multipartFile.getOriginalFilename(), httpUrl);
            } else {

                //is not Photo
                list.add("");

                //Insert Log
                PhotoLog.error(request, multipartFile.getOriginalFilename());
            }
        }
        return list;
    }


}
