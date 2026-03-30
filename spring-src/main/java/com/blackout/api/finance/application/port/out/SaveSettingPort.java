package com.blackout.api.finance.application.port.out;

import com.blackout.api.settings.domain.Setting;

public interface SaveSettingPort {
    Setting save(Setting setting);
}
