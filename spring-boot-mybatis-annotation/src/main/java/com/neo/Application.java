package com.neo;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neo.mapper")
@EnableMethodCache(basePackages = "com.neo")
@EnableCreateCacheAnnotation
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
