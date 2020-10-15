package com.neo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * API文档生成工具；访问地址： http://localhost:端口/swagger-ui.html basePackage
 * 必须包括**，否则与feign扫描冲突
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2.class);
	@Value("${enable.swagger}")
	private boolean enableSwagger;

	@Value("${base.package.swagger}")
	private String basePackage;

	@Bean
	public Docket createRestApiBase() {
		LOGGER.info("Swagger2信息: enable.swagger=" + enableSwagger + "; base.package.swagger=" + basePackage);
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(enableSwagger)
				.groupName("delay").apiInfo(apiInfoBase()).select()
				.apis(RequestHandlerSelectors.basePackage(basePackage + ".delay"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfoBase() {
		return new ApiInfoBuilder().title("rabbit mq")
				.description("这是rabbit mq demo")
				.termsOfServiceUrl("")
				.version("1.0").build();
	}

	@Bean
	public Docket createRestApiDirect() {
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(enableSwagger)
				.groupName("direct").apiInfo(apiInfoDirect()).select()
				.apis(RequestHandlerSelectors.basePackage(basePackage + ".direct"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfoDirect() {
		return new ApiInfoBuilder().title("rabbit mq")
				.description("Direct 模式")
				.termsOfServiceUrl("")
				.version("1.0").build();
	}


	@Bean
	public Docket createRestApiFanout() {
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(enableSwagger)
				.groupName("fanout").apiInfo(apiInfoFanout()).select()
				.apis(RequestHandlerSelectors.basePackage(basePackage + ".fanout"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfoFanout() {
		return new ApiInfoBuilder().title("rabbit mq")
				.description("Fanout 模式")
				.termsOfServiceUrl("")
				.version("1.0").build();
	}

}