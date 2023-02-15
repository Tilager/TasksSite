package ru.kanatov.site;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;

@SpringBootApplication
public class SiteApplication {
    @Value("${upload_path}")
    private String uploadPath;

    public static void main(String[] args) {
        SpringApplication.run(SiteApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        new File(uploadPath + "/files/").mkdirs();
        new File(uploadPath + "/achievements/").mkdirs();
    }
}
