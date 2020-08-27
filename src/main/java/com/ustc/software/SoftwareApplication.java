package com.ustc.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//配置类
//@EnableSwagger2
@SpringBootApplication
public class SoftwareApplication {
	public static void main(String[] args) {
		SpringApplication.run(SoftwareApplication.class, args);
	}

}
