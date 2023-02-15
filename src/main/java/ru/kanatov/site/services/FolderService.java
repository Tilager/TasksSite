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
        File folder = new File(uploadPath + "/files/");

        return Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(File::isDirectory).toList();
    }

    public String createFolder() {
        int count = 0;
        while (true) {
            String nameFolder = "Новая папка" + (count == 0 ? "" : " (" +  count + ")");
            String uuidFolder = UUID.nameUUIDFromBytes(nameFolder.getBytes()).toString().substring(0, 13);
            String resultFolderName = uuidFolder + "_" + nameFolder;
            File folder = new File(uploadPath + "/files/" + resultFolderName);
            if (!folder.exists()) {
                if (folder.mkdirs())
                    return resultFolderName;
                else
                    throw new CreateFolderException("Folder not created.");
            }

            count++;
        }
    }

    public String uploadFile(MultipartFile file, String folderId) throws IOException {
        File folder = Objects.requireNonNull(new File(uploadPath + "/files/").listFiles((dir, name) -> name.startsWith(folderId)))[0];

        if (!file.isEmpty()) {
            String fileName = UUID.nameUUIDFromBytes(Objects.requireNonNull(file.getOriginalFilename()).getBytes()).toString().substring(0, 13) + "_" + file.getOriginalFilename();
            file.transferTo(new File(folder + "/" + fileName));
            return fileName;
        }

        return "";
    }

    public boolean removeFolder(String folderId) {
        File folder = Objects.requireNonNull(new File(uploadPath + "/files/").listFiles((dir, name) -> name.startsWith(folderId)))[0];

        try {
            FileUtils.deleteDirectory(folder);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean removeFile(String folderId, String fileId) throws IOException {
        File folder = Objects.requireNonNull(new File(uploadPath + "/files/").listFiles((dir, name) -> name.startsWith(folderId)))[0];
        File file = Objects.requireNonNull(folder.listFiles(((dir, name) -> name.startsWith(fileId))))[0];

        return Files.deleteIfExists(file.toPath());
    }

    public String rename(String folderId, String newName) {
        String newFolderId = UUID.nameUUIDFromBytes(newName.getBytes()).toString().substring(0, 13);
        File folder = Objects.requireNonNull(new File(uploadPath + "/files/").listFiles((dir, name) -> name.startsWith(folderId)))[0];
        File resultFolder = new File(folder.getParent() + "/" + newFolderId + "_" + newName);

        if (resultFolder.exists()) {
            return "";
        } else {
            return folder.renameTo(resultFolder) ? resultFolder.getName() : "";
        }
    }
}
