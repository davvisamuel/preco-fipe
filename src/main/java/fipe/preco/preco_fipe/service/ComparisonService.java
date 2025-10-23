package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.Comparison;
import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.ComparisonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComparisonService {

    private final ComparisonRepository comparisonRepository;

    public Comparison save(Comparison comparison) {
        return comparisonRepository.save(comparison);
    }

    public Comparison findByIdAndUser(Integer id, User user) {
        return comparisonRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Comparison not found"));
    }

    public void delete(Integer id, User user) {
        var comparison = findByIdAndUser(id, user);

        comparisonRepository.delete(comparison);
    }

    public Page<Comparison> findAllPaginated(User user, Pageable pageable) {
        return comparisonRepository.findAllByUser(user, pageable);
    }
}
