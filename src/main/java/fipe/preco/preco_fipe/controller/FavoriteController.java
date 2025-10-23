package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.mapper.FavoriteMapper;
import fipe.preco.preco_fipe.request.FavoritePostRequest;
import fipe.preco.preco_fipe.response.FavoriteGetResponse;
import fipe.preco.preco_fipe.response.FavoritePostResponse;
import fipe.preco.preco_fipe.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteMapper favoriteMapper;

    @PostMapping
    public ResponseEntity<FavoritePostResponse> save(@AuthenticationPrincipal User user, @RequestBody @Valid FavoritePostRequest favoritePostRequest) {
        log.debug("Request received for '{}'", favoritePostRequest);

        String codeFipe = favoritePostRequest.getCodeFipe();

        var favorite = favoriteService.save(user, codeFipe);

        var favoritePostResponse = favoriteMapper.toFavoritePostResponse(favorite);

        return ResponseEntity.status(HttpStatus.CREATED).body(favoritePostResponse);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<FavoriteGetResponse>> findAllPaginated(@AuthenticationPrincipal User user, @ParameterObject Pageable pageable) {
        log.debug("Request received to list all favorites paginated");

        var favoriteGetResponsePage = favoriteService.findAllPaginated(user, pageable).map(favoriteMapper::toFavoriteGetResponse);

        return ResponseEntity.ok(favoriteGetResponsePage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        log.debug("request received for delete favorite");

        favoriteService.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}
