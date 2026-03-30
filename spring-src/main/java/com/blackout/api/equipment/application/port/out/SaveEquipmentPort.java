package com.blackout.api.equipment.application.port.out;

import com.blackout.api.equipment.domain.*;

public interface SaveEquipmentPort {

    BandMember saveMember(BandMember m);
    void deleteMemberById(String id);

    Instrument saveInstrument(Instrument i);
    void deleteInstrumentById(String id);

    Amplifier saveAmplifier(Amplifier a);
    void deleteAmplifierById(String id);

    Microphone saveMicrophone(Microphone m);
    void deleteMicrophoneById(String id);

    PaEquipment savePa(PaEquipment p);
    void deletePaById(String id);

    // Bulk mic assignment operations
    void clearMicAssignment(String bandId, String assignedToType, String assignedToId);
    void bulkAssignMics(String bandId, String assignedToType, String assignedToId, java.util.List<String> micIds);

    // Instrument–amplifier link operations
    void clearInstrumentAmpLink(String ampId, String bandId);
    void setInstrumentAmpLink(String ampId, String instrumentId, String bandId);
}
