package com.blackout.api.finance.infrastructure.persistence;

import com.blackout.api.settings.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface JpaSettingRepository extends JpaRepository<Setting, String> {

    Optional<Setting> findByBandIdAndKey(String bandId, String key);
}
