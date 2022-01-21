package com.art.artweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = {"com.art"})
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.art.artcommon.mapper"})
public class ArtWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtWebApplication.class, args);
    }

}
