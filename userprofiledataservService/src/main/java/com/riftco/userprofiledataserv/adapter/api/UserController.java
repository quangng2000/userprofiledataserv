package com.riftco.userprofiledataserv.adapter.api;

import com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase;
import com.riftco.userprofiledataserv.application.port.in.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.riftco.userprofiledataserv.application.port.in.ModifyUserUseCase.*;
import static com.riftco.userprofiledataserv.application.port.in.CreateUserUseCase.*;

@RestController
@RequestMapping(value = "v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ModifyUserUseCase modifyUserUseCase;

    @PostMapping
    public void create(@RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(
                request.getName(),
                request.getEmail(),
                request.getContactNumber());
        this.createUserUseCase.createUser(command);
    }

    @PutMapping("/{uuid}/changeName")
    public void changeName(@PathVariable UUID uuid, @RequestBody ChangeNameUserRequest request) {
        ChangeNameUserCommand command = new ChangeNameUserCommand(
                uuid,
                request.getName());
        this.modifyUserUseCase.changeName(command);
    }


}
