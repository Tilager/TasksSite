package ru.kanatov.site.services;

import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kanatov.site.models.AchievementModel;
import ru.kanatov.site.repositories.AchievementRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Log4j2
public class AchievementsService {
    @Value("${upload_path}")
    private String uploadPath;

    private final AchievementRepository achievementRepository;

    @Autowired
    public AchievementsService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    public List<AchievementModel> getAllAchievements() {
        return achievementRepository.findAllByType(AchievementModel.TYPE_ACHIEVEMENT);
    }

    public List<AchievementModel> getAllCourses() {
        return achievementRepository.findAllByType(AchievementModel.TYPE_COURSE);
    }

    public List<AchievementModel> getAllSciences() {
        return achievementRepository.findAllByType(AchievementModel.TYPE_SCIENCE);
    }

    public boolean removeAchievement(String name) throws IOException {
        AchievementModel achievement = achievementRepository.findByTypeAndName(AchievementModel.TYPE_ACHIEVEMENT, name + ".pdf");
        File file = new File(uploadPath + "/achievements/achievements/" + achievement.getUuid() + ".pdf");
        achievementRepository.delete(achievement);

        return Files.deleteIfExists(file.toPath());
    }

    public boolean removeCourse(String name) throws IOException {
        AchievementModel achievement = achievementRepository.findByTypeAndName(AchievementModel.TYPE_COURSE, name + ".pdf");
        File file = new File(uploadPath + "/achievements/courses/" + achievement.getUuid() + ".pdf");
        achievementRepository.delete(achievement);

        return Files.deleteIfExists(file.toPath());
    }

    public boolean removeScience(String name) throws IOException {
        AchievementModel achievement = achievementRepository.findByTypeAndName(AchievementModel.TYPE_SCIENCE, name + ".pdf");
        File file = new File(uploadPath + "/achievements/sciences/" + achievement.getUuid() + ".pdf");
        achievementRepository.delete(achievement);

        return Files.deleteIfExists(file.toPath());
    }

    public boolean uploadAchievement(MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            String uuid = String.valueOf(UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes()));
            File folder = new File(uploadPath + "/achievements/achievements/");

            AchievementModel achievement = new AchievementModel();
            achievement.setUuid(uuid);
            achievement.setName(file.getOriginalFilename());
            achievement.setType(AchievementModel.TYPE_ACHIEVEMENT);
            try {
                achievementRepository.save(achievement);
            } catch (DataIntegrityViolationException e) {
                return false;
            }

            file.transferTo(new File(folder, uuid + ".pdf"));
        } else {
            return false;
        }

        return true;
    }

    public boolean uploadCourse(MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            String uuid = String.valueOf(UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes()));
            File folder = new File(uploadPath + "/achievements/courses/");
            AchievementModel achievement = new AchievementModel();
            achievement.setUuid(uuid);
            achievement.setName(file.getOriginalFilename());
            achievement.setType(AchievementModel.TYPE_COURSE);
            try {
                achievementRepository.save(achievement);
            } catch (DataIntegrityViolationException e) {
                return false;
            }

            file.transferTo(new File(folder, uuid + ".pdf"));
        } else {
            return false;
        }

        return true;
    }

    public boolean uploadScience(MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            String uuid = String.valueOf(UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes()));
            File folder = new File(uploadPath + "/achievements/sciences/");
            AchievementModel achievement = new AchievementModel();
            achievement.setUuid(uuid);
            achievement.setName(file.getOriginalFilename());
            achievement.setType(AchievementModel.TYPE_SCIENCE);

            try {
                achievementRepository.save(achievement);
            } catch (DataIntegrityViolationException e) {
                return false;
            }

            file.transferTo(new File(folder, uuid + ".pdf"));
        } else {
            return false;
        }

        return true;
    }
}
