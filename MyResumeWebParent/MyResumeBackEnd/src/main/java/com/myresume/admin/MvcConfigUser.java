package com.myresume.admin;

//import java.nio.file.Path;
//import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfigUser implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String dirName = "user-photos";
        Path userProfilePhotosDir = Paths.get(dirName);

        String userProfilePhotosPath = userProfilePhotosDir.toFile().getAbsolutePath();
//		String userProfilePhotosPath = "D:\\backup_proiecte\\my_java_resume\\MyResumeProject\\MyResumeWebParent\\MyResumeBackEnd\\profile-photos";
//		System.out.print(userProfilePhotosPath);
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+userProfilePhotosPath +
                "/");
//		registry.addResourceHandler("/myresumeadmin/**").addResourceLocations( "localhost:8080/myresumeadmin");
//		 registry.addResourceHandler("/profile-photos/**").addResourceLocations("classpath:/profile-photos/");
    }

}
