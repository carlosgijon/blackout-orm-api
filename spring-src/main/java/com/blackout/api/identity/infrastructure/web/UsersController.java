package com.blackout.api.identity.infrastructure.web;

import com.blackout.api.identity.application.service.UsersApplicationService;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
public class UsersController {

    private final UsersApplicationService usersService;

    public UsersController(UsersApplicationService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<UserResponse> findAll(BlackoutAuthentication auth) {
        requireBandId(auth);
        return usersService.findAll(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(BlackoutAuthentication auth,
                                @Valid @RequestBody CreateUserRequest req) {
        requireBandId(auth);
        return usersService.create(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public UserResponse update(BlackoutAuthentication auth, @PathVariable String id,
                                @RequestBody UpdateUserRequest req) {
        requireBandId(auth);
        return usersService.update(auth.getBandId(), id, req);
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(BlackoutAuthentication auth, @PathVariable String id,
                                @Valid @RequestBody ChangePasswordRequest req) {
        requireBandId(auth);
        usersService.changePassword(auth.getBandId(), id, req.newPassword());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        requireBandId(auth);
        usersService.remove(auth.getBandId(), id, auth.getUserId());
    }

    private void requireBandId(BlackoutAuthentication auth) {
        if (auth.getBandId() == null)
            throw new ForbiddenException("Token sin banda seleccionada.");
    }
}
