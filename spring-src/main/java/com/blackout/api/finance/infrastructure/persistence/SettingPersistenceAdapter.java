package com.blackout.api.finance.infrastructure.persistence;

import com.blackout.api.finance.application.port.out.LoadSettingPort;
import com.blackout.api.finance.application.port.out.SaveSettingPort;
import com.blackout.api.settings.domain.Setting;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("financeSettingPersistenceAdapter")
class SettingPersistenceAdapter implements LoadSettingPort, SaveSettingPort {

    private final JpaSettingRepository repo;

    SettingPersistenceAdapter(JpaSettingRepository repo) {
        this.repo = repo;
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
