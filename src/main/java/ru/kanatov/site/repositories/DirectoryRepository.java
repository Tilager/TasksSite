package ru.kanatov.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kanatov.site.models.DirectoryModel;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryModel, Integer> {
   @Query(value = "SELECT d.name from DirectoryModel d where d.name LIKE concat(:startText, '%') ")
   List<String> findNamesByNameStartingWith(String startText);
   DirectoryModel findByUuid(String uuid);
}
