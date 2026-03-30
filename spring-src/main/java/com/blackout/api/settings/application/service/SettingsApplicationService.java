package com.blackout.api.settings.application.service;

import com.blackout.api.settings.application.port.out.LoadSettingPort;
import com.blackout.api.settings.application.port.out.SaveSettingPort;
import com.blackout.api.settings.domain.Setting;
import com.blackout.api.settings.infrastructure.web.dto.SettingsResponse;
import com.blackout.api.settings.infrastructure.web.dto.UpdateSettingsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SettingsApplicationService {

    private final LoadSettingPort loadSetting;
    private final SaveSettingPort saveSetting;

    public SettingsApplicationService(LoadSettingPort loadSetting, SaveSettingPort saveSetting) {
        this.loadSetting = loadSetting;
        this.saveSetting = saveSetting;
    }

    public SettingsResponse getSettings(String bandId) {
        List<Setting> settings = loadSetting.findAllByBandId(bandId);
        Map<String, String> map = settings.stream()
                .collect(Collectors.toMap(Setting::getKey, Setting::getValue));

        return new SettingsResponse(
                map.getOrDefault("theme", "dark"),
                map.get("bpmApiKey"),
                map.get("fontSize"),
                map.get("fontFamily")
        );
    }

    @Transactional
    public void updateSettings(String bandId, UpdateSettingsRequest request) {
        if (request.theme() != null) upsert(bandId, "theme", request.theme());
        if (request.bpmApiKey() != null) upsert(bandId, "bpmApiKey", request.bpmApiKey());
        if (request.fontSize() != null) upsert(bandId, "fontSize", request.fontSize());
        if (request.fontFamily() != null) upsert(bandId, "fontFamily", request.fontFamily());
    }

    private void upsert(String bandId, String key, String value) {
        loadSetting.findByBandIdAndKey(bandId, key)
                .ifPresentOrElse(
                        existing -> {
                            existing.setValue(value);
                            saveSetting.save(existing);
                        },
                        () -> saveSetting.save(new Setting(bandId, key, value))
                );
    }
}
