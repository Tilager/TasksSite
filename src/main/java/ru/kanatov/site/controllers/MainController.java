package ru.kanatov.site.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kanatov.site.services.AchievementsService;
import ru.kanatov.site.services.FolderService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Controller
@RequestMapping("/")
@Log4j2
public class MainController {
    @Value("${upload_path}")
    private String uploadPath;

    private final AchievementsService achievementsService;
    private final FolderService folderService;

    @Autowired
    public MainController(AchievementsService achievementsService, FolderService folderService) {
        this.achievementsService = achievementsService;
        this.folderService = folderService;
    }

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String authorization() {
        return "authorization";
    }

    @GetMapping("/achievements")
    public String achievements(Model model) {
        model.addAttribute("courses", achievementsService.getAllCourses());
        model.addAttribute("achievements", achievementsService.getAllAchievements());
        model.addAttribute("sciences", achievementsService.getAllSciences());

        return "achievements";
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        Map<String, List<String>> allFolders = new HashMap<>() {{
            for (File folder : folderService.getAllFolders()) {
                List<String> fileNames = new ArrayList<>();
                for (File file : Objects.requireNonNull(folder.listFiles())) {
                    fileNames.add(file.getName());
                }

                put(folder.getName(), fileNames);
            }
        }};

        model.addAttribute("allFolders", allFolders);

        return "tasks";
    }

    @GetMapping("/download/{folderId}/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("folderId") String folderId,
                                                 @PathVariable("fileId") String fileId) {
        File folder = Objects.requireNonNull(new File(uploadPath).listFiles((dir, name) -> name.startsWith(folderId)))[0];
        File file = Objects.requireNonNull(folder.listFiles(((dir, name) -> name.startsWith(fileId))))[0];
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName().substring(14));

        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
