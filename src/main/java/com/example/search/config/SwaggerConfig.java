package com.example.search.config;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class configures the swagger with all the details.
 * @author vivek thakur
 *
 */
@Configuration
@EnableSwagger2
@EnableAsync
public class SwaggerConfig {
	/**
	 * @return {@link Docket}.
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.search.rest.v1"))
				.paths(PathSelectors.regex("/content.*")).build().apiInfo(metaData());
	}

	/**
	 * Sets the meta data aabout application.
	 * @return
	 */
	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("Search Content Service",
				"This service searches the books in google api and albums from apple api and returns both in case of search called for some content",
				"v1", "/search/", new springfox.documentation.service.Contact("Vivek Thakur", "", ""),
				"@copy right reservered to vivek", "", new ArrayList<VendorExtension>());
		return apiInfo;
	}
	
	@Bean(name = "asyncExecutor")
    public Executor asyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
}