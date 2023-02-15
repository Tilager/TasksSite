package ru.kanatov.site.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Log4j2
public class AchievementsService {
    @Value("${upload_path}")
    private String uploadPath;

    public List<String> getAllAchievements() {
        File folder = new File(uploadPath + "/achievements/achievements/");
        List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(File::isFile).toList();

        return new ArrayList<>() {{
            for (File f: files) {
                add(f.getName());
            }
        }};
    }

    public List<String> getAllCourses() {
        File folder = new File(uploadPath + "/achievements/courses/");
        List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(File::isFile).toList();

        return new ArrayList<>() {{
            for (File f: files) {
                add(f.getName());
            }
        }};
    }

    public List<String> getAllSciences() {
        File folder = new File(uploadPath + "/achievements/sciences/");
        List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(File::isFile).toList();

        return new ArrayList<>() {{
            for (File f: files) {
                add(f.getName());
            }
        }};
    }

    public boolean removeAchievement(String name) throws IOException {
        File file = new File(uploadPath + "/achievements/achievements/" + name + ".pdf");

        return Files.deleteIfExists(file.toPath());
    }

    public boolean removeCourse(String name) throws IOException {
        File file = new File(uploadPath + "/achievements/courses/" + name + ".pdf");

        return Files.deleteIfExists(file.toPath());
    }

    public boolean removeScience(String name) throws IOException {
        File file = new File(uploadPath + "/achievements/sciences/" + name + ".pdf");

        return Files.deleteIfExists(file.toPath());
    }

    public boolean uploadAchievement(MultipartFile file) throws IOException {
        File folder = new File(uploadPath + "/achievements/achievements/");

        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            file.transferTo(new File(folder, file.getOriginalFilename()));
        } else {
            return false;
        }

        return true;
    }

    public boolean uploadCourse(MultipartFile file) throws IOException {
        File folder = new File(uploadPath + "/achievements/courses/");

        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            file.transferTo(new File(folder, file.getOriginalFilename()));
        } else {
            return false;
        }

        return true;
    }

    public boolean uploadScience(MultipartFile file) throws IOException {
        File folder = new File(uploadPath + "/achievements/sciences/");

        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            file.transferTo(new File(folder, file.getOriginalFilename()));
        } else {
            return false;
        }

        return true;
    }
}
