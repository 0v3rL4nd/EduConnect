package com.piattaforme;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
@EnableAsync
public class EduConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduConnectApplication.class, args);
    }
}
