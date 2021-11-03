package com.myresume.site;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableEncryptableProperties
@EntityScan({"com.myresume.common.entity","com.myresume.site","com.myresume.admin"})
public class MyResumeFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyResumeFrontEndApplication.class, args);
	}

}
