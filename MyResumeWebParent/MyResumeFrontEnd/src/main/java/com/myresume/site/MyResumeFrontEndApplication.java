package com.myresume.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.myresume.common.entity"})
public class MyResumeFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyResumeFrontEndApplication.class, args);
	}

}
