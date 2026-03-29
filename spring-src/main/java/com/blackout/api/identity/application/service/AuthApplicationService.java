package com.blackout.api.identity.application.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.blackout.api.identity.application.port.out.*;
import com.blackout.api.identity.domain.*;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.*;
import com.blackout.api.shared.infrastructure.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class AuthApplicationService {

    private final LoadUserPort loadUser;
    private final LoadBandPort loadBand;
    private final LoadUserBandPort loadUserBand;
    private final JwtService jwt;

    public AuthApplicationService(LoadUserPort loadUser, LoadBandPort loadBand,
                                   LoadUserBandPort loadUserBand, JwtService jwt) {
        this.loadUser = loadUser;
        this.loadBand = loadBand;
        this.loadUserBand = loadUserBand;
        this.jwt = jwt;
    }

    public LoginResponse login(String username, String password) {
        User user = loadUser.findByUsername(username)
            .filter(User::isActive)
            .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash());
        if (!result.verified) throw new BadCredentialsException("Credenciales incorrectas");

        List<UserBand> memberships = loadUserBand.findAllByUserId(user.getId());
        List<BandInfo> bands = memberships.stream().map(m -> {
            Band band = loadBand.findById(m.getBandId()).orElseThrow();
            return BandInfo.from(band, m.getRole());
        }).toList();

        String bandIdForToken = bands.size() == 1 ? bands.get(0).id() : null;
        String token = buildToken(user, user.getRole(), bandIdForToken);

        return new LoginResponse(token, UserResponse.from(user), bands);
    }

    public SelectBandResponse selectBand(String userId, String bandId) {
        UserBand membership = loadUserBand.findByUserIdAndBandId(userId, bandId)
            .orElseThrow(() -> new ForbiddenException("No perteneces a esta banda"));

        User user = loadUser.findById(userId)
            .filter(User::isActive)
            .orElseThrow(() -> new ForbiddenException("Usuario inactivo"));

        Band band = loadBand.findById(bandId).orElseThrow();
        String token = buildToken(user, membership.getRole(), bandId);

        return new SelectBandResponse(token, UserResponse.from(user), BandInfo.from(band, membership.getRole()));
    }

    public MeResponse getMe(String userId, String bandId) {
        User user = loadUser.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        BandInfo bandInfo = null;
        if (bandId != null) {
            Band band = loadBand.findById(bandId).orElse(null);
            if (band != null) {
                String role = loadUserBand.findByUserIdAndBandId(userId, bandId)
                    .map(UserBand::getRole)
                    .orElse(user.getRole());
                bandInfo = BandInfo.from(band, role);
            }
        }

        return new MeResponse(UserResponse.from(user), bandInfo);
    }

    private String buildToken(User user, String role, String bandId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", role);
        if (bandId != null) claims.put("bandId", bandId);
        return jwt.generateToken(claims, user.getId());
    }
}
