package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.domain.User;
import fipe.preco.preco_fipe.mapper.ConsultationMapper;
import fipe.preco.preco_fipe.dto.response.ConsultationGetResponse;
import fipe.preco.preco_fipe.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/consultation")
@RequiredArgsConstructor
@Slf4j
public class ConsultationController {
    private final ConsultationService consultationService;
    private final ConsultationMapper consultationMapper;

    @GetMapping
    public ResponseEntity<Page<ConsultationGetResponse>> findAllPaginated(Pageable pageable) {
        log.debug("Request received for '{}'", pageable);

        var consultationPage = consultationService.findAllPaginated(pageable);

        var consultationGetResponsePage = consultationPage.map(consultationMapper::toConsultationGetResponse);

        return ResponseEntity.ok(consultationGetResponsePage);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        log.debug("Request received for delete consultation '{}'", id);

        consultationService.delete(id, user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllByUser(@AuthenticationPrincipal User user) {
        log.debug("Request received for delete all consultations of '{}'", user);

        consultationService.deleteAllByUser(user);

        return ResponseEntity.noContent().build();
    }

}
