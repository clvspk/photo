package com.clvspk.photo.utils;

import com.clvspk.photo.config.Config;
import com.clvspk.photo.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class PhotoUtils {

    @Autowired
    private Config config;


    private String basePath = null;

    private Long overTime = null;


    /**
     *  Photo Server Storage Path
     */
    public String getBasePath() {
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
                        Result.fail("File Dir Create Error");
                        System.out.println("File Dir Create Error :" + file.getPath());
                    }
                }
                this.basePath = file.getPath();
                this.overTime = Date.from(LocalDateTime.now().plusDays(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .atZone(ZoneId.systemDefault()).toInstant()).getTime();
            }
        }
        return this.basePath;
    }


    /**
     * Splice http Url
     */
    public String getHttpUrl(String filePath) {
        return config.getLocalhost() +"/"
                + filePath.replace(config.getBasePath(), "").replace("\\", "/");
    }


    /**
     * Is Photo
     */
    public static boolean isPhoto(InputStream inputStream) {
        try {
            Image image = ImageIO.read(inputStream);
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Create UUID Name
     */
    public static String getPhotoName(MultipartFile multipartFile) {
        return UUID.randomUUID().toString().replace("-", "")
                + multipartFile.getOriginalFilename().substring(
                multipartFile.getOriginalFilename().lastIndexOf("."),
                multipartFile.getOriginalFilename().length()
        );
    }
}
