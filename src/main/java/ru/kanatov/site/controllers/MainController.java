package ru.kanatov.site.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kanatov.site.services.AchievementsService;
import ru.kanatov.site.services.FolderService;

import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/")
@Log4j2
public class MainController {
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
}
