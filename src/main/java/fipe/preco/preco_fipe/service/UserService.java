package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.Role;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.UserEmailPutRequest;
import fipe.preco.preco_fipe.dto.request.UserPasswordPutRequest;
import fipe.preco.preco_fipe.exception.EmailAlreadyExistsException;
import fipe.preco.preco_fipe.exception.InvalidPasswordException;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void delete(User authenticatedUser) {
        repository.delete(authenticatedUser);
    }

    public void update(User authenticatedUser, User userToUpdate) {
        var savedUser = findById(authenticatedUser.getId());

        userToUpdate.setId(authenticatedUser.getId());

        userToUpdate.setRole(savedUser.getRole());

        userToUpdate.setEmail(userToUpdate.getEmail() == null ?
                savedUser.getEmail() : userToUpdate.getEmail());

        userToUpdate.setPassword(userToUpdate.getPassword() == null ?
                savedUser.getPassword() : passwordEncoder.encode(userToUpdate.getPassword()));

        assertEmailDoesNotExist(userToUpdate.getEmail(), savedUser.getId());

        repository.save(userToUpdate);
    }

    public void updateEmail(User user, UserEmailPutRequest userEmailPutRequest) {
        var rawPassword = userEmailPutRequest.getCurrentPassword();

        var newEmail = userEmailPutRequest.getNewEmail();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidPasswordException("Senha atual incorreta");
        }

        assertEmailDoesNotExist(newEmail, user.getId());

        user.setEmail(newEmail);

        repository.save(user);
    }

    public void updatePassword(User user, UserPasswordPutRequest userPasswordPutRequest) {
        var currentPassword = userPasswordPutRequest.getCurrentPassword();

        var newPassword = userPasswordPutRequest.getNewPassword();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Senha atual incorreta");
        }

        var passwordEncoded = passwordEncoder.encode(newPassword);

        user.setPassword(passwordEncoded);

        repository.save(user);
    }

    public void assertUserExist(Integer id) {
        findById(id);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailAlreadyExistsException);
    }

    public void assertEmailDoesNotExist(String email, Integer id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailAlreadyExistsException);
    }

    public void throwEmailAlreadyExistsException(User user) {
        throw new EmailAlreadyExistsException("Email '%s' already exists".formatted(user.getEmail()));
    }
}
