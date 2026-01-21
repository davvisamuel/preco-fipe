package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.dto.request.UserEmailPutRequest;
import fipe.preco.preco_fipe.dto.request.UserPasswordPutRequest;
import fipe.preco.preco_fipe.exception.EmailAlreadyExistsException;
import fipe.preco.preco_fipe.exception.InvalidPasswordException;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("findAll returns user page when successful")
    @Order(1)
    void findAll_ReturnsUserPage_WhenSuccessful() {
        var expectedUserPage = UserUtils.newPageUser();

        var pageable = expectedUserPage.getPageable();

        BDDMockito.when(repository.findAll(pageable)).thenReturn(expectedUserPage);

        var userPage = service.findAll(pageable);

        Assertions.assertThat(userPage)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(expectedUserPage)
                .doesNotContainNull();
    }

    @Test
    @DisplayName("findAll returns an empty list when successful")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenSuccessful() {
        var unpaged = Pageable.unpaged();

        BDDMockito.when(repository.findAll(unpaged)).thenReturn(new PageImpl<>(Collections.emptyList()));

        var userList = service.findAll(unpaged);

        Assertions.assertThat(userList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save creates a user when successful")
    @Order(3)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = UserUtils.newUserToSave();

        var expectedUserSaved = UserUtils.newSavedUser();

        var encodedPassword = expectedUserSaved.getPassword();

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
        var userToSave = UserUtils.newUserToSave();

        var savedUser = UserUtils.newSavedUser();

        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("findById returns a user when id is found")
    @Order(5)
    void findById_ReturnsUser_WhenIdIsFound() {
        var savedUser = UserUtils.newSavedUser();

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
        var id = 99;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("delete removes a user when successful")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {
        var authenticatedUser = UserUtils.newSavedUser();

        BDDMockito.doNothing().when(repository).delete(authenticatedUser);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.delete(authenticatedUser));
    }

    @Test
    @DisplayName("update updates a user when id is found")
    @Order(8)
    void update_UpdatesUser_WhenSuccessful() {
        var authenticatedUser = UserUtils.newSavedUser();

        var savedUser = UserUtils.newSavedUser();

        var userToUpdate = UserUtils.newUserToUpdate();

        BDDMockito.when(repository.findById(authenticatedUser.getId())).thenReturn(Optional.of(savedUser));

        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), savedUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(authenticatedUser, userToUpdate));
    }

    @Test
    @DisplayName("update ThrowsEmailAlreadyExistsException when email already exists")
    @Order(9)
    void update_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        var authenticatedUser = UserUtils.newSavedUser();

        var savedUser = UserUtils.newSavedUser();

        var userToUpdate = UserUtils.newUserToUpdate();

        var existingUser = UserUtils.newUserToUpdate();

        BDDMockito.when(repository.findById(authenticatedUser.getId())).thenReturn(Optional.of(savedUser));

        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), savedUser.getId())).thenReturn(Optional.of(existingUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(authenticatedUser, userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @Order(10)
    @DisplayName("updateEmail update email when successful")
    void updateEmail_updatesEmail_WhenSuccessful() {
        var authenticatedUser = UserUtils.newSavedUser();

        var currentPassword = UserUtils.rawPasswordOfSavedUser();

        var newEmail = "newEmail@example.com";

        var userEmailPutRequest = UserEmailPutRequest.builder()
                .currentPassword(currentPassword)
                .newEmail(newEmail)
                .build();

        BDDMockito.when(passwordEncoder.matches(userEmailPutRequest.getCurrentPassword(), authenticatedUser.getPassword()))
                .thenReturn(true);

        BDDMockito.when(repository.findByEmailAndIdNot(newEmail, authenticatedUser.getId())).thenReturn(Optional.empty());

        BDDMockito.when(repository.save(authenticatedUser.withEmail(newEmail))).thenReturn(authenticatedUser.withEmail(newEmail));

        Assertions.assertThatNoException().isThrownBy(() -> service.updateEmail(authenticatedUser, userEmailPutRequest));
    }

    @Test
    @Order(11)
    @DisplayName("updateEmail throws EmailAlreadyExistsException when email already exists")
    void updateEmail_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        var authenticatedUser = UserUtils.newSavedUser();

        var currentPassword = UserUtils.rawPasswordOfSavedUser();

        var userThatAlreadyHasEmail = UserUtils.newUserList().getLast();

        var newEmail = userThatAlreadyHasEmail.getEmail();

        var userEmailPutRequest = UserEmailPutRequest.builder()
                .currentPassword(currentPassword)
                .newEmail(newEmail)
                .build();

        BDDMockito.when(passwordEncoder.matches(userEmailPutRequest.getCurrentPassword(), authenticatedUser.getPassword()))
                .thenReturn(true);

        BDDMockito.when(repository.findByEmailAndIdNot(newEmail, authenticatedUser.getId())).thenReturn(Optional.of(userThatAlreadyHasEmail));

        Assertions.assertThatException().isThrownBy(() -> service.updateEmail(authenticatedUser, userEmailPutRequest))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @Order(12)
    @DisplayName("updateEmail throws InvalidPasswordException when password is incorrect")
    void updateEmail_ThrowsInvalidPasswordException_WhenIncorrectPassword() {
        var authenticatedUser = UserUtils.newSavedUser();

        var invalidRawPassword = "incorrectPassword";

        var newEmail = "newEmail@example.com";

        var userEmailPutRequest = UserEmailPutRequest.builder()
                .currentPassword(invalidRawPassword)
                .newEmail(newEmail)
                .build();

        BDDMockito.when(passwordEncoder.matches(userEmailPutRequest.getCurrentPassword(), authenticatedUser.getPassword()))
                .thenReturn(false);

        Assertions.assertThatException().isThrownBy(() -> service.updateEmail(authenticatedUser, userEmailPutRequest))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @Order(13)
    @DisplayName("updatePassword update password when successful")
    void updatePassword_updatesPassword_WhenSuccessful() {
        var authenticatedUser = UserUtils.newSavedUser();

        var currentPassword = UserUtils.rawPasswordOfSavedUser();

        var newPassword = "newPassword";

        var userPasswordPutRequest = UserPasswordPutRequest.builder()
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .build();

        BDDMockito.when(passwordEncoder.matches(userPasswordPutRequest.getCurrentPassword(), authenticatedUser.getPassword()))
                .thenReturn(true);

        var passwordEncoded = "encrypt";

        BDDMockito.when(passwordEncoder.encode(newPassword)).thenReturn(passwordEncoded);

        BDDMockito.when(repository.save(authenticatedUser.withPassword(passwordEncoded))).thenReturn(authenticatedUser.withPassword(passwordEncoded));

        Assertions.assertThatNoException().isThrownBy(() -> service.updatePassword(authenticatedUser, userPasswordPutRequest));
    }

    @Test
    @Order(14)
    @DisplayName("updatePassword throws InvalidPasswordException when password is incorrect")
    void updatePassword_ThrowsInvalidPasswordException_WhenIncorrectPassword() {
        var authenticatedUser = UserUtils.newSavedUser();

        var invalidRawPassword = "incorrectPassword";

        var newPassword = "newPassword";

        var userPasswordPutRequest = UserPasswordPutRequest.builder()
                .currentPassword(invalidRawPassword)
                .newPassword(newPassword)
                .build();

        BDDMockito.when(passwordEncoder.matches(userPasswordPutRequest.getCurrentPassword(), authenticatedUser.getPassword()))
                .thenReturn(false);

        Assertions.assertThatException().isThrownBy(() -> service.updatePassword(authenticatedUser, userPasswordPutRequest))
                .isInstanceOf(InvalidPasswordException.class);
    }
}