package ru.kanatov.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kanatov.site.models.AchievementModel;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementModel, Integer> {
    List<AchievementModel> findAllByType(int type);
    AchievementModel findByTypeAndName(int type, String name);
}
