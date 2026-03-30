package com.blackout.api.equipment.application.port.out;

import com.blackout.api.equipment.domain.*;

import java.util.List;
import java.util.Optional;

public interface LoadEquipmentPort {

    List<BandMember> findAllMembersByBandId(String bandId);
    Optional<BandMember> findMemberByIdAndBandId(String id, String bandId);

    List<Instrument> findAllInstrumentsByBandId(String bandId);
    Optional<Instrument> findInstrumentByIdAndBandId(String id, String bandId);

    List<Amplifier> findAllAmplifiersByBandId(String bandId);
    Optional<Amplifier> findAmplifierByIdAndBandId(String id, String bandId);

    List<Microphone> findAllMicrophonesByBandId(String bandId);
    Optional<Microphone> findMicrophoneByIdAndBandId(String id, String bandId);

    List<PaEquipment> findAllPaByBandId(String bandId);
    Optional<PaEquipment> findPaByIdAndBandId(String id, String bandId);
}
