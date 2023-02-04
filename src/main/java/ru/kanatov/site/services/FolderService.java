package ru.kanatov.site.services;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kanatov.site.exceptions.CreateFolderException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Log4j2
public class FolderService {
    @Value("${upload_path}")
    private String uploadPath;

    public List<File> getAllFolders() {
        File folder = new File(uploadPath);

        if (!folder.exists() && !folder.mkdir())
            log.error("Error create upload files folder!");

        return Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(File::isDirectory).toList();
    }

    public String createFolder(String name) {
        String resultName = UUID.nameUUIDFromBytes(name.getBytes()).toString().substring(0, 13) + "_" + name;
        File folder = new File(uploadPath + "/" + resultName);

        if (folder.exists()) {
            log.error("A folder with this name already exists");
            return "";
        } else if (!folder.mkdir()) {
            log.error("Error create folder!");
            throw new CreateFolderException("Error create upload files folder!");
        }

        return resultName;
    }

    public String uploadFile(MultipartFile file, String folderId) throws IOException {
        File folder = Objects.requireNonNull(new File(uploadPath).listFiles((dir, name) -> name.startsWith(folderId)))[0];

        if (!file.isEmpty()) {
            String fileName = UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes()).toString().substring(0, 13) + "_" + file.getOriginalFilename();
            file.transferTo(new File(folder + "/" + fileName));
            return fileName;
        }

        return "";
    }

    public boolean removeFolder(String folderId) {
        File folder = Objects.requireNonNull(new File(uploadPath).listFiles((dir, name) -> name.startsWith(folderId)))[0];

        try {
            FileUtils.deleteDirectory(folder);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean removeFile(String folderId, String fileId) throws IOException {
        File folder = Objects.requireNonNull(new File(uploadPath).listFiles((dir, name) -> name.startsWith(folderId)))[0];
        File file = Objects.requireNonNull(folder.listFiles(((dir, name) -> name.startsWith(fileId))))[0];

        return Files.deleteIfExists(file.toPath());
    }

    public boolean rename(String folderId, String newName) {
        File folder = Objects.requireNonNull(new File(uploadPath).listFiles((dir, name) -> name.startsWith(folderId)))[0];
        File resultName = new File(folder.getParent() + "/" + folderId + "_" + newName);

        return folder.renameTo(resultName);
    }
}
