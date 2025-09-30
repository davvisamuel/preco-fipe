package fipe.preco.preco_fipe.controller;

import fipe.preco.preco_fipe.mapper.UserMapper;
import fipe.preco.preco_fipe.request.UserPostRequest;
import fipe.preco.preco_fipe.request.UserPutRequest;
import fipe.preco.preco_fipe.response.UserGetResponse;
import fipe.preco.preco_fipe.response.UserPostResponse;
import fipe.preco.preco_fipe.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@AllArgsConstructor
@Log4j2
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll() {
        var allUsers = service.findAll();

        var userGetResponseList = mapper.toUserGetResponseList(allUsers);

        return ResponseEntity.ok(userGetResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        var user = service.findById(id);

        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        log.debug("Request received for '{}'", userPostRequest);

        var user = mapper.toUser(userPostRequest);

        var savedUser = service.save(user);

        var userPostResponse = mapper.toUserPostResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }


    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest userPutRequest) {
        var userToUpdate = mapper.toUser(userPutRequest);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        service.delete();
        return ResponseEntity.noContent().build();
    }
}
