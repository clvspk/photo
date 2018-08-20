package com.clvspk.photo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "imgConfig")
public class Config {

    private String localhost;

    private String basePath;

    @ManagedAttribute
    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    @ManagedAttribute
    public String getBasePath() {
        return File.separator + basePath.replace(":", File.separator) + File.separator;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
