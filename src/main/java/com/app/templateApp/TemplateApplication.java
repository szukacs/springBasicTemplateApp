package com.app.templateApp;

import com.app.templateApp.util.FileConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
        new File(FileConstant.USER_FOLDER).mkdirs();
    }
}
