package com.blackout.api.settings.infrastructure.persistence;

import com.blackout.api.settings.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface JpaSettingRepository extends JpaRepository<Setting, String> {
    List<Setting> findAllByBandId(String bandId);
    Optional<Setting> findByBandIdAndKey(String bandId, String key);
}
