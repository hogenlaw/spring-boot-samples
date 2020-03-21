package com.example.shardingsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.example.shardingsphere.mapper")
public class SpringBootShardingSphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShardingSphereApplication.class, args);
    }

}
