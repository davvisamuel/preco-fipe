package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.mapper.ComparisonMapper;
import fipe.preco.preco_fipe.response.ComparisonGetResponse;
import fipe.preco.preco_fipe.response.ComparisonPostResponse;
import fipe.preco.preco_fipe.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comparison")
@RequiredArgsConstructor
@Slf4j
public class ComparisonController {

    private final ComparisonService comparisonService;
    private final ComparisonMapper comparisonMapper;

    @PostMapping
    private ResponseEntity<ComparisonPostResponse> save(@AuthenticationPrincipal User user) {
        log.debug("Request received for creates comparison");

        var comparisonToSave = comparisonMapper.toComparison(user);

        var savedComparison = comparisonService.save(comparisonToSave);

        var comparisonPostResponse = comparisonMapper.toComparisonPostResponse(savedComparison);

        return ResponseEntity.status(HttpStatus.CREATED).body(comparisonPostResponse);
    }

    @DeleteMapping("{id}")
    private ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        log.debug("Request received for delete comparison where id '{}'", id);

        comparisonService.delete(id, user);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    private ResponseEntity<Page<ComparisonGetResponse>> findAllPaginated(@AuthenticationPrincipal User user, Pageable pageable) {
        log.debug("Request received for '{}'", pageable);

        var comparisonPage = comparisonService.findAllPaginated(user, pageable);

        var comparisonGetResponsePage = comparisonPage.map(comparisonMapper::toComparisonGetResponse);

        return ResponseEntity.ok(comparisonGetResponsePage);
    }
}
