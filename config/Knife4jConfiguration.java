package com.example.totaldemo.config;


import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


@Slf4j
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    /**
     * 【重要】指定Controller包路径
     */
    private String basePackage = "com.example.totaldemo.controller";
    /**
     * 分组名称
     */
    private String groupName = "product";
    /**
     * 主机名
     */
    private String host = "http://zhouDaBeautiful";
    /**
     * 标题
     */
    private String title = "周大漂亮的小店--自恋图书馆";
    /**
     * 简介
     */
    private String description = "专门卖点周大漂亮的书,主打的就是一手自恋与欣赏";
    /**
     * 服务条款URL
     */
    private String termsOfServiceUrl = "http://www.apache.org/licenses/LICENSE-2.0";
    /**
     * 联系人
     */
    private String contactName = "樊大帅气";
    /**
     * 联系网址
     */
    private String contactUrl = "没有网址,别看了";
    /**
     * 联系邮箱
     */
    private String contactEmail = "fzl17713324195@163.com";
    /**
     * 版本号
     */
    private String version = "1.0.0";

    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    public Knife4jConfiguration() {
        log.debug("加载配置类：Knife4jConfiguration");
    }

    @Bean
    public Docket docket() {
        String groupName = "1.0.0";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsOfServiceUrl)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .version(version)
                .build();
    }
}
