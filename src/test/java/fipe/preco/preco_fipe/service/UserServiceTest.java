package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.EmailAlreadyExistsException;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.UserRepository;
import fipe.preco.preco_fipe.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserUtils userUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("findAll returns all users when successful")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(userUtils.newUserList());

        var expectedUserList = userUtils.newUserList();

        var userList = service.findAll();

        Assertions.assertThat(userList)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(expectedUserList)
                .doesNotContainNull();
    }

    @Test
    @DisplayName("findAll returns an empty list when successful")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(Collections.emptyList());

        var userList = service.findAll();

        Assertions.assertThat(userList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save creates a user when successful")
    @Order(3)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();

        var encodedPassword = "$2a$10$QvLzMZj0H3V7Z6p2Zv8F/O3FzXKkF2xY5Hq0jM2FsC3v1P7U7k0lO";

        var expectedUserSaved = userUtils.newSavedUser();

        BDDMockito.when(passwordEncoder.encode(userToSave.getPassword())).thenReturn(encodedPassword);

        BDDMockito.when(repository.save(userToSave)).thenReturn(expectedUserSaved);

        User savedUser = service.save(userToSave);

        Assertions.assertThat(savedUser)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(expectedUserSaved);
    }

    @Test
    @DisplayName("save ThrowsEmailAlreadyExistsException when email already exists")
    @Order(4)
    void save_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        var userToSave = userUtils.newUserToSave();

        var savedUser = userUtils.newSavedUser();

        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("findById returns a user when id is found")
    @Order(5)
    void findById_ReturnsUser_WhenIdIsFound() {
        var savedUser = userUtils.newSavedUser();

        var id = savedUser.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(savedUser));

        var user = service.findById(id);

        Assertions.assertThat(user)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(savedUser);
    }

    @Test
    @DisplayName("findById ThrowsNotFoundException when id is not found")
    @Order(6)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() {
        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("delete removes a user when successful")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var authenticatedUser = userUtils.newSavedUser();

        setSecurityContext(authenticatedUser);

        BDDMockito.doNothing().when(repository).delete(authenticatedUser);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.delete());
    }

    @Test
    @DisplayName("update updates a user when id is found")
    @Order(8)
    void update_UpdatesUser_WhenSuccessful() {
        var authenticatedUser = userUtils.newSavedUser();

        setSecurityContext(authenticatedUser);

        var savedUser = userUtils.newSavedUser();

        var userToUpdate = userUtils.newUserToUpdate();

        BDDMockito.when(repository.findById(authenticatedUser.getId())).thenReturn(Optional.of(savedUser));

        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), savedUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update ThrowsEmailAlreadyExistsException when email already exists")
    @Order(9)
    void update_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        var authenticatedUser = userUtils.newSavedUser();

        setSecurityContext(authenticatedUser);

        var savedUser = userUtils.newSavedUser();

        var userToUpdate = userUtils.newUserToUpdate();

        var existingUser = userUtils.newUserToUpdate();

        BDDMockito.when(repository.findById(authenticatedUser.getId())).thenReturn(Optional.of(savedUser));

        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), savedUser.getId())).thenReturn(Optional.of(existingUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    public void setSecurityContext(User authenticatedUser) {
        SecurityContextHolder.setContext(securityContext);

        BDDMockito.when(securityContext.getAuthentication())
                .thenReturn(authentication);

        BDDMockito.when(authentication.getPrincipal())
                .thenReturn(authenticatedUser);
    }
}