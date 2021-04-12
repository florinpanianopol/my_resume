package com.myresume.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.myresume.common.entity","com.myresume.admin.section"})
public class MyResumeBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyResumeBackEndApplication.class, args);
	}

}
