package ru.kanatov.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kanatov.site.models.FileModel;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Integer> {
    FileModel findByUuidAndDirectoryUuid(String fileUUID, String directoryUUID);
}
