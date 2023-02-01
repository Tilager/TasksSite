package ru.kanatov.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kanatov.site.models.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String s);
}
