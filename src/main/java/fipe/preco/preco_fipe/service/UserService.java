package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.EmailAlreadyExistsException;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void delete() {
        var authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        repository.delete(authenticatedUser);
    }

    public void update(User userToUpdate) {
        var authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var savedUser = findById(authenticatedUser.getId());

        userToUpdate.setId(authenticatedUser.getId());

        userToUpdate.setRoles(savedUser.getRoles());

        userToUpdate.setEmail(userToUpdate.getEmail() == null ?
                savedUser.getEmail() : userToUpdate.getEmail());

        userToUpdate.setPassword(userToUpdate.getPassword() == null ?
                savedUser.getPassword() : passwordEncoder.encode(userToUpdate.getPassword()));

        assertEmailDoesNotExist(userToUpdate.getEmail(), savedUser.getId());

        repository.save(userToUpdate);
    }

    public void assertUserExist(Long id) {
        findById(id);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailAlreadyExistsException);
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailAlreadyExistsException);
    }

    public void throwEmailAlreadyExistsException(User user) {
        throw new EmailAlreadyExistsException("Email '%s' already exists".formatted(user.getEmail()));
    }
}
