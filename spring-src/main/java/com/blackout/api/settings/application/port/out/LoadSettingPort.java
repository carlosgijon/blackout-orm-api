package com.blackout.api.settings.application.port.out;

import com.blackout.api.settings.domain.Setting;
import java.util.List;
import java.util.Optional;

public interface LoadSettingPort {
    List<Setting> findAllByBandId(String bandId);
    Optional<Setting> findByBandIdAndKey(String bandId, String key);
}
