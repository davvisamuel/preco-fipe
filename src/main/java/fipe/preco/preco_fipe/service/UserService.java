package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.EmailAlreadyExistsException;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void delete(Long id) {
        var userToDelete = findById(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        var savedUser = findById(userToUpdate.getId());

        userToUpdate.setRoles(savedUser.getRoles());

        if(userToUpdate.getEmail() == null) {
            userToUpdate.setEmail(savedUser.getEmail());
        }

        if(userToUpdate.getPassword() == null) {
            userToUpdate.setPassword(savedUser.getPassword());
        }

        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());

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
