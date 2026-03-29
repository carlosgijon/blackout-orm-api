package com.blackout.api.identity.application.factory;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.blackout.api.identity.application.port.out.*;
import com.blackout.api.identity.domain.*;
import com.blackout.api.identity.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BandFactory {

    private final SaveBandPort saveBand;
    private final LoadBandPort loadBand;
    private final SaveUserPort saveUser;
    private final LoadUserPort loadUser;
    private final LoadUserBandPort loadUserBand;

    public BandFactory(SaveBandPort saveBand, LoadBandPort loadBand,
                       SaveUserPort saveUser, LoadUserPort loadUser,
                       LoadUserBandPort loadUserBand) {
        this.saveBand = saveBand;
        this.loadBand = loadBand;
        this.saveUser = saveUser;
        this.loadUser = loadUser;
        this.loadUserBand = loadUserBand;
    }

    @Transactional
    public CreateBandResponse createWithAdmin(CreateBandRequest req) {
        if (loadBand.findBySlug(req.slug()).isPresent())
            throw new ConflictException("El slug \"" + req.slug() + "\" ya está en uso");
        if (loadUser.existsByUsername(req.adminUsername()))
            throw new ConflictException("El usuario \"" + req.adminUsername() + "\" ya existe");

        Band band = saveBand.save(new Band(req.name(), req.slug()));

        String hash = BCrypt.withDefaults().hashToString(10, req.adminPassword().toCharArray());
        User admin = new User(req.adminUsername(), hash, "admin");
        admin.setBandId(band.getId());
        admin = saveUser.save(admin);

        loadUserBand.save(new UserBand(admin.getId(), band.getId(), "admin"));

        return new CreateBandResponse(
            band.getId(), band.getName(), band.getSlug(), band.getCreatedAt(),
            new CreateBandResponse.AdminUserSummary(admin.getId(), admin.getUsername())
        );
    }
}
