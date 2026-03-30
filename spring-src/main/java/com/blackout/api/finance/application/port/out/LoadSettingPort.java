package com.blackout.api.finance.application.port.out;

import com.blackout.api.settings.domain.Setting;
import java.util.Optional;

public interface LoadSettingPort {
    Optional<Setting> findByBandIdAndKey(String bandId, String key);
}
