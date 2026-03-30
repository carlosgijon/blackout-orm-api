package com.blackout.api.settings.infrastructure.persistence;

import com.blackout.api.settings.application.port.out.LoadSettingPort;
import com.blackout.api.settings.application.port.out.SaveSettingPort;
import com.blackout.api.settings.domain.Setting;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class SettingPersistenceAdapter implements LoadSettingPort, SaveSettingPort {

    private final JpaSettingRepository repo;

    SettingPersistenceAdapter(JpaSettingRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Setting> findAllByBandId(String bandId) {
        return repo.findAllByBandId(bandId);
    }

    @Override
    public Optional<Setting> findByBandIdAndKey(String bandId, String key) {
        return repo.findByBandIdAndKey(bandId, key);
    }

    @Override
    public Setting save(Setting setting) {
        return repo.save(setting);
    }
}
