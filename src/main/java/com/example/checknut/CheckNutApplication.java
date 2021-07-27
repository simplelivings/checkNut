package com.example.checknut;

import com.example.checknut.controller.WebSocketServer;
import com.example.checknut.entity.ReturnInfo;
import com.example.checknut.listener.FileMonitor;
import com.example.checknut.utils.OpenCVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableScheduling
@CrossOrigin
public class CheckNutApplication {

    @Value("${selfDefinition.imagePath}")
    private String imagePath;

    public static void main(String[] args) {
        //1 连接openCV库
        OpenCVUtils.loadOpenCV();
        SpringApplication.run(CheckNutApplication.class, args);

    }

    @Bean
    public FileMonitor fileMonitor() {
        return new FileMonitor(imagePath);
    }

}
