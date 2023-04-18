package ru.kanatov.site.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kanatov.site.exceptions.CreateFolderException;
import ru.kanatov.site.models.FileModel;
import ru.kanatov.site.services.AchievementsService;
import ru.kanatov.site.services.FolderService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<?> addFolder() {
        try {
            return new ResponseEntity<>(folderService.createFolder(), HttpStatus.OK);
        } catch (CreateFolderException ex) {
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> addFile(@RequestParam("file") MultipartFile file,
                          @RequestParam("folderId") String folderId) {
        try {
            FileModel res = folderService.uploadFile(file, folderId);

            if (res != null)
                return new ResponseEntity<>(res, HttpStatus.OK);
            else
                return new ResponseEntity<>("Upload file error", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Upload file error", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> rename(@RequestParam("folderId") String folderId, @RequestParam("name") String newName) {
        Map<String, String> changedFolder = folderService.rename(folderId, newName);
        if (changedFolder != null)
            return new ResponseEntity<>(changedFolder, HttpStatus.OK);
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
    public ResponseEntity<?> uploadAchievement(@RequestParam("file") MultipartFile file) {
        try {
            if (!achievementsService.uploadAchievement(file)) {
                return new ResponseEntity<>("Расширение файла не PDF или файл уже существует!", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }

        Map<String, String> response = new HashMap<>() {{
           put("name", file.getOriginalFilename());
           put("uuid", String.valueOf(UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes())));
        }};

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/uploadCourse")
    public ResponseEntity<?> uploadCourse(@RequestParam("file") MultipartFile file) {
        try {
            if (!achievementsService.uploadCourse(file)) {
                return new ResponseEntity<>("Расширение файла не PDF или файл уже существует!", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }

        Map<String, String> response = new HashMap<>() {{
            put("name", file.getOriginalFilename());
            put("uuid", String.valueOf(UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes())));
        }};

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/uploadScience")
    public ResponseEntity<?> uploadScience(@RequestParam("file") MultipartFile file) {
        try {
            if (!achievementsService.uploadScience(file)) {
                return new ResponseEntity<>("Расширение файла не PDF или файл уже существует!", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("IOException", HttpStatus.BAD_REQUEST);
        }

        Map<String, String> response = new HashMap<>() {{
            put("name", file.getOriginalFilename());
            put("uuid", String.valueOf(UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes())));
        }};

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
