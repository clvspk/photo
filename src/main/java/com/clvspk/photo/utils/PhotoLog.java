package com.clvspk.photo.utils;

import javax.servlet.http.HttpServletRequest;

public class PhotoLog {

    public static void success(HttpServletRequest request, String fileName, String httpUrl) {
        System.out.println(
                String.format("Success Ip: %s , SourceFileName: %s , HttpUrl: %s , Time: %s",
                        IpAddr.getIpAddr(request),
                        fileName,
                        httpUrl,
                        DateUtils.now()));
    }

    public static void error(HttpServletRequest request, String fileName) {
        System.out.println
                (String.format("Error Ip: %s , FileError: %s , Time: %s",
                        IpAddr.getIpAddr(request), fileName,
                        DateUtils.now()));
    }
}
