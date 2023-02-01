package ru.kanatov.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kanatov.site.models.UserModel;
import ru.kanatov.site.repositories.UserRepository;
import ru.kanatov.site.security.UserDetailsIm;

import java.util.Optional;

@Service
public class UserDetailsServiceIm implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceIm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> op = userRepository.findByUsername(username);

        if (op.isEmpty())
            throw new UsernameNotFoundException("User not found.");

        return new UserDetailsIm(op.get());
    }
}
