package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.ComparisonRepository;
import fipe.preco.preco_fipe.utils.ComparisonUtils;
import fipe.preco.preco_fipe.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComparisonServiceTest {

    @InjectMocks
    private ComparisonService comparisonService;

    @Mock
    private ComparisonRepository comparisonRepository;


    @Test
    @DisplayName("save creates comparison when successfull")
    @Order(1)
    void save_CreatesComparison_WhenSuccessful() {
        var user = UserUtils.newSavedUser();

        var comparisonToSave = ComparisonUtils.newComparisonToSave(user);

        var expectedComparisonSaved = ComparisonUtils.newComparisonSaved(user);

        BDDMockito.when(comparisonService.save(comparisonToSave))
                .thenReturn(expectedComparisonSaved);

        var comparisonSaved = comparisonService.save(comparisonToSave);

        Assertions.assertThat(comparisonSaved)
                .isNotNull()
                .isEqualTo(expectedComparisonSaved);
    }

    @Test
    @DisplayName("findByIdAndUser returns comparison when comparison is found")
    @Order(2)
    void findByIdAndUser_ReturnsComparison_WhenComparisonIsFound() {
        var user = UserUtils.newSavedUser();

        var expectedComparisonSaved = ComparisonUtils.newComparisonSaved(user);

        var id = expectedComparisonSaved.getId();

        BDDMockito.when(comparisonRepository.findByIdAndUser(id, user))
                .thenReturn(Optional.of(expectedComparisonSaved));

        var comparison = comparisonService.findByIdAndUser(id, user);

        Assertions.assertThat(comparison)
                .isNotNull()
                .isEqualTo(expectedComparisonSaved);
    }

    @Test
    @DisplayName("findByIdAndUser Throws NotFoundException when comparison not found")
    @Order(3)
    void findByIdAndUser_ThrowsNotFoundException_WhenComparisonNotFound() {
        var id = 99;

        var user = User.builder().build();

        BDDMockito.when(comparisonRepository.findByIdAndUser(id, user))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> comparisonService.findByIdAndUser(id, user))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("delete removes comparison when comparison is found")
    @Order(4)
    void delete_RemovesComparison_WhenComparisonIsFound() {
        var user = UserUtils.newSavedUser();

        var comparisonSaved = ComparisonUtils.newComparisonSaved(user);

        var id = comparisonSaved.getId();

        BDDMockito.when(comparisonRepository.findByIdAndUser(id, user))
                .thenReturn(Optional.of(comparisonSaved));

        BDDMockito.doNothing().when(comparisonRepository).delete(comparisonSaved);

        Assertions.assertThatNoException()
                .isThrownBy(() -> comparisonService.delete(id, user));
    }

    @Test
    @DisplayName("delete removes comparison when comparison is found")
    @Order(5)
    void delete_ThrowsNotFoundException_WhenComparisonNotFound() {
        var id = 99;

        var user = User.builder().build();

        BDDMockito.when(comparisonRepository.findByIdAndUser(id, user))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> comparisonService.delete(id, user))
                .isInstanceOf(NotFoundException.class);
    }

    //findAllPaginated
}