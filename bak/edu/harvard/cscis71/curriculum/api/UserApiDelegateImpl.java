package edu.harvard.cscis71.curriculum.api;

import edu.harvard.cscis71.curriculum.model.User;
import edu.harvard.cscis71.curriculum.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserApiDelegateImpl implements UserApiDelegate {
    private final UserRepository userRepository;

    public UserApiDelegateImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Void> createUser(User user) {
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<User> getUserByName(String username) {
        return userRepository.findById(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> updateUser(String username, User user) {
        user.setUsername(username);
        return createUser(user);
    }
}
