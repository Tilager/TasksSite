package ru.kanatov.site.services;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kanatov.site.exceptions.CreateFolderException;
import ru.kanatov.site.models.DirectoryModel;
import ru.kanatov.site.models.FileModel;
import ru.kanatov.site.repositories.DirectoryRepository;
import ru.kanatov.site.repositories.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@Log4j2
public class FolderService {
    @Value("${upload_path}")
    private String uploadPath;

    private final DirectoryRepository directoryRepository;
    private final FileRepository fileRepository;

    @Autowired
    public FolderService(DirectoryRepository directoryRepository, FileRepository fileRepository) {
        this.directoryRepository = directoryRepository;
        this.fileRepository = fileRepository;
    }

    public List<DirectoryModel> getAllFolders() {
        return directoryRepository.findAll(Sort.by("name"));
    }

    public DirectoryModel createFolder() {
        List<String> newFolders = directoryRepository.findNamesByNameStartingWith("Новая папка");
        String nameFolder = null, uuidFolder;

        for (int i=0; i <= newFolders.size(); ++i) {
            nameFolder = "Новая папка" + (i == 0 ? "" : " (" +  i + ")");

            if (!newFolders.contains(nameFolder))
                break;

        }

        uuidFolder = UUID.nameUUIDFromBytes(nameFolder.getBytes()).toString();

        DirectoryModel directory = new DirectoryModel();
        directory.setUuid(uuidFolder);
        directory.setName(nameFolder);

        directoryRepository.save(directory);
        File folder = new File(uploadPath + "/files/" + uuidFolder);
        if (!folder.exists())
            if (folder.mkdirs())
                return directory;

        throw new CreateFolderException("Folder not created.");
    }

    public FileModel uploadFile(MultipartFile file, String folderUUID) throws IOException {
        if (file != null) {
            File folder = new File(uploadPath
                    + "/files/"
                    + folderUUID);

            String fileUUID = UUID.nameUUIDFromBytes(Objects.requireNonNull(file.getOriginalFilename()).getBytes()).toString();
            String fileUri = folder + "/" + fileUUID + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

            if (new File(fileUri).exists())
                return null;
            else {
                FileModel fileModel = new FileModel();
                fileModel.setDirectory(directoryRepository.findByUuid(folderUUID));
                fileModel.setUuid(fileUUID);
                fileModel.setName(file.getOriginalFilename());
                fileRepository.save(fileModel);

                file.transferTo(new File(fileUri));
                return fileModel;
            }
        }

        return null;
    }

    public boolean removeFolder(String folderId) {
        File folder = new File(uploadPath + "/files/" + folderId);


        try {
            FileUtils.deleteDirectory(folder);
            DirectoryModel directory = directoryRepository.findByUuid(folderId);
            directoryRepository.delete(directory);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean removeFile(String folderId, String fileId) throws IOException {
        FileModel file = fileRepository.findByUuidAndDirectoryUuid(fileId, folderId);
        File folder = new File(uploadPath + "/files/" + folderId + "/" + fileId + file.getName().substring(file.getName().lastIndexOf('.')));

        if (Files.deleteIfExists(folder.toPath())) {
            fileRepository.delete(file);
            return true;
        } else return false;

    }

    public Map<String, String> rename(String folderId, String newName) {
        String newFolderId = UUID.nameUUIDFromBytes(newName.getBytes()).toString();

        File folder = new File(uploadPath + "/files/" + folderId);
        File resultFolder = new File(uploadPath + "/files/" + newFolderId
        );

        if (resultFolder.exists()) {
            return null;
        } else {
            if (folder.renameTo(resultFolder)) {
                DirectoryModel directory = directoryRepository.findByUuid(folderId);
                directory.setName(newName);
                directory.setUuid(newFolderId);
                directoryRepository.save(directory);

                return new HashMap<>() {{
                    put("uuid", directory.getUuid());
                    put("name", directory.getName());
                }};
            } else return null;
        }
    }
}
