package com.blackout.api.identity.application.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.blackout.api.identity.application.port.out.*;
import com.blackout.api.identity.domain.*;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsersApplicationService {

    private final LoadUserPort loadUser;
    private final SaveUserPort saveUser;
    private final LoadUserBandPort loadUserBand;

    public UsersApplicationService(LoadUserPort loadUser, SaveUserPort saveUser,
                                    LoadUserBandPort loadUserBand) {
        this.loadUser = loadUser;
        this.saveUser = saveUser;
        this.loadUserBand = loadUserBand;
    }

    public List<UserResponse> findAll(String bandId) {
        return loadUser.findAllByBandId(bandId).stream()
            .map(UserResponse::from)
            .toList();
    }

    @Transactional
    public UserResponse create(String bandId, CreateUserRequest req) {
        if (loadUser.existsByUsername(req.username()))
            throw new ConflictException("El usuario \"" + req.username() + "\" ya existe");

        String hash = BCrypt.withDefaults().hashToString(10, req.password().toCharArray());
        User user = new User(req.username(), hash, req.role());
        user.setDisplayName(req.displayName());
        user.setBandId(bandId);
        user = saveUser.save(user);
        loadUserBand.save(new UserBand(user.getId(), bandId, req.role()));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse update(String bandId, String id, UpdateUserRequest req) {
        User user = findOwnedUser(bandId, id);

        if (req.username() != null && !req.username().equals(user.getUsername())) {
            if (loadUser.existsByUsername(req.username()))
                throw new ConflictException("El usuario \"" + req.username() + "\" ya existe");
            user.setUsername(req.username());
        }
        if (req.displayName() != null) user.setDisplayName(req.displayName());
        if (req.role() != null)        user.setRole(req.role());
        if (req.isActive() != null)    user.setActive(req.isActive());

        return UserResponse.from(saveUser.save(user));
    }

    @Transactional
    public void changePassword(String bandId, String id, String newPassword) {
        User user = findOwnedUser(bandId, id);
        String hash = BCrypt.withDefaults().hashToString(10, newPassword.toCharArray());
        user.setPasswordHash(hash);
        saveUser.save(user);
    }

    @Transactional
    public void remove(String bandId, String id, String requesterId) {
        if (id.equals(requesterId))
            throw new ForbiddenException("No puedes eliminar tu propio usuario");
        User user = findOwnedUser(bandId, id);
        saveUser.deleteById(user.getId());
    }

    private User findOwnedUser(String bandId, String id) {
        return loadUser.findById(id)
            .filter(u -> bandId.equals(u.getBandId()))
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}
