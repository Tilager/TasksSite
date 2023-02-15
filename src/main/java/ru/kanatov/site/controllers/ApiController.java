package ru.kanatov.site.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kanatov.site.exceptions.CreateFolderException;
import ru.kanatov.site.services.AchievementsService;
import ru.kanatov.site.services.FolderService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Log4j2
public class ApiController {
    private final FolderService folderService;
    private final AchievementsService achievementsService;

    @Autowired
    public ApiController(FolderService folderService, AchievementsService achievementsService) {
        this.folderService = folderService;
        this.achievementsService = achievementsService;
    }

    @PostMapping("/addFolder")
    public ResponseEntity<String> addFolder() {
        try {
            return new ResponseEntity<>(folderService.createFolder(), HttpStatus.OK);
        } catch (CreateFolderException ex) {
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/uploadFile")
    public String addFile(@RequestParam("file") MultipartFile file,
                          @RequestParam("folderId") String folderId) {
        try {
            String res = folderService.uploadFile(file, folderId);

            if (!res.equals(""))
                return res;
            else
                throw new RuntimeException("Upload file error");
        } catch (IOException e) {
            throw new RuntimeException("Upload file error");
        }
    }

    @PostMapping("/removeFolder")
    public ResponseEntity<String> removeFolder(@RequestBody String folderId) {
        if (folderService.removeFolder(folderId))
            return new ResponseEntity<>("Folder success removed!", HttpStatus.OK);
        else
            return new ResponseEntity<>("Folder not removed!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/removeFile")
    public ResponseEntity<String> removeFile(@RequestParam("folderId") String folderId, @RequestParam("fileId") String fileId) {
        try {
            if (folderService.removeFile(folderId, fileId)) {
                return new ResponseEntity<>("File successes removed.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("File not removed.", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rename")
    public ResponseEntity<String> rename(@RequestParam("folderId") String folderId, @RequestParam("name") String newName) {
        String changedName = folderService.rename(folderId, newName);
        if (!changedName.equals(""))
            return new ResponseEntity<>(changedName, HttpStatus.OK);
        else
            return new ResponseEntity<>("Folder not renamed.", HttpStatus.BAD_REQUEST);
    }

    // achievements

    @PostMapping("/removeAchievement")
    public ResponseEntity<String> removeAchievement(@RequestBody String name) {
        try {
            if (achievementsService.removeAchievement(name)) {
                return new ResponseEntity<>(name, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("File not deleted!", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/removeCourse")
    public ResponseEntity<String> removeCourse(@RequestBody String name) {
        try {
            if (achievementsService.removeCourse(name)) {
                return new ResponseEntity<>(name, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("File not deleted!", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/removeScience")
    public ResponseEntity<String> removeScience(@RequestBody String name) {
        try {
            if (achievementsService.removeScience(name)) {
                return new ResponseEntity<>(name, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("File not deleted!", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/uploadAchievement")
    public ResponseEntity<String> uploadAchievement(@RequestParam("file") MultipartFile file) {
        try {
            if (!achievementsService.uploadAchievement(file)) {
                return new ResponseEntity<>("File extension != pdf", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(file.getOriginalFilename(), HttpStatus.OK);
    }

    @PostMapping("/uploadCourse")
    public ResponseEntity<String> uploadCourse(@RequestParam("file") MultipartFile file) {
        try {
            if (!achievementsService.uploadCourse(file)) {
                return new ResponseEntity<>("File extension != pdf", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(file.getOriginalFilename(), HttpStatus.OK);
    }

    @PostMapping("/uploadScience")
    public ResponseEntity<String> uploadScience(@RequestParam("file") MultipartFile file) {
        try {
            if (!achievementsService.uploadScience(file)) {
                return new ResponseEntity<>("File extension != pdf", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(file.getOriginalFilename(), HttpStatus.OK);
    }
}
