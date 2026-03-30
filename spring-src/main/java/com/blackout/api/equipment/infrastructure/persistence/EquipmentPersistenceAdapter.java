package com.blackout.api.equipment.infrastructure.persistence;

import com.blackout.api.equipment.application.port.out.LoadEquipmentPort;
import com.blackout.api.equipment.application.port.out.SaveEquipmentPort;
import com.blackout.api.equipment.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EquipmentPersistenceAdapter implements LoadEquipmentPort, SaveEquipmentPort {

    private final JpaBandMemberRepository memberRepo;
    private final JpaInstrumentRepository instrumentRepo;
    private final JpaAmplifierRepository amplifierRepo;
    private final JpaMicrophoneRepository microphoneRepo;
    private final JpaPaEquipmentRepository paRepo;

    public EquipmentPersistenceAdapter(JpaBandMemberRepository memberRepo,
                                       JpaInstrumentRepository instrumentRepo,
                                       JpaAmplifierRepository amplifierRepo,
                                       JpaMicrophoneRepository microphoneRepo,
                                       JpaPaEquipmentRepository paRepo) {
        this.memberRepo = memberRepo;
        this.instrumentRepo = instrumentRepo;
        this.amplifierRepo = amplifierRepo;
        this.microphoneRepo = microphoneRepo;
        this.paRepo = paRepo;
    }

    // ── BandMember ────────────────────────────────────────────────────────────

    @Override
    public List<BandMember> findAllMembersByBandId(String bandId) {
        return memberRepo.findAllByBandIdOrderBySortOrderAsc(bandId);
    }

    @Override
    public Optional<BandMember> findMemberByIdAndBandId(String id, String bandId) {
        return memberRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public BandMember saveMember(BandMember m) {
        return memberRepo.save(m);
    }

    @Override
    public void deleteMemberById(String id) {
        memberRepo.deleteById(id);
    }

    // ── Instrument ────────────────────────────────────────────────────────────

    @Override
    public List<Instrument> findAllInstrumentsByBandId(String bandId) {
        return instrumentRepo.findAllByBandIdOrderByChannelOrderAsc(bandId);
    }

    @Override
    public Optional<Instrument> findInstrumentByIdAndBandId(String id, String bandId) {
        return instrumentRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public Instrument saveInstrument(Instrument i) {
        return instrumentRepo.save(i);
    }

    @Override
    public void deleteInstrumentById(String id) {
        instrumentRepo.deleteById(id);
    }

    // ── Amplifier ─────────────────────────────────────────────────────────────

    @Override
    public List<Amplifier> findAllAmplifiersByBandId(String bandId) {
        return amplifierRepo.findAllByBandId(bandId);
    }

    @Override
    public Optional<Amplifier> findAmplifierByIdAndBandId(String id, String bandId) {
        return amplifierRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public Amplifier saveAmplifier(Amplifier a) {
        return amplifierRepo.save(a);
    }

    @Override
    public void deleteAmplifierById(String id) {
        amplifierRepo.deleteById(id);
    }

    // ── Microphone ────────────────────────────────────────────────────────────

    @Override
    public List<Microphone> findAllMicrophonesByBandId(String bandId) {
        return microphoneRepo.findAllByBandId(bandId);
    }

    @Override
    public Optional<Microphone> findMicrophoneByIdAndBandId(String id, String bandId) {
        return microphoneRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public Microphone saveMicrophone(Microphone m) {
        return microphoneRepo.save(m);
    }

    @Override
    public void deleteMicrophoneById(String id) {
        microphoneRepo.deleteById(id);
    }

    // ── PaEquipment ───────────────────────────────────────────────────────────

    @Override
    public List<PaEquipment> findAllPaByBandId(String bandId) {
        return paRepo.findAllByBandId(bandId);
    }

    @Override
    public Optional<PaEquipment> findPaByIdAndBandId(String id, String bandId) {
        return paRepo.findByIdAndBandId(id, bandId);
    }

    @Override
    public PaEquipment savePa(PaEquipment p) {
        return paRepo.save(p);
    }

    @Override
    public void deletePaById(String id) {
        paRepo.deleteById(id);
    }

    // ── Bulk mic assignment ───────────────────────────────────────────────────

    @Override
    public void clearMicAssignment(String bandId, String assignedToType, String assignedToId) {
        microphoneRepo.clearAssignment(bandId, assignedToType, assignedToId);
    }

    @Override
    public void bulkAssignMics(String bandId, String assignedToType, String assignedToId,
                               List<String> micIds) {
        microphoneRepo.bulkAssign(bandId, assignedToType, assignedToId, micIds);
    }

    // ── Instrument–amplifier link ─────────────────────────────────────────────

    @Override
    public void clearInstrumentAmpLink(String ampId, String bandId) {
        instrumentRepo.clearAmpIdByAmpId(ampId, bandId);
    }

    @Override
    public void setInstrumentAmpLink(String ampId, String instrumentId, String bandId) {
        instrumentRepo.setAmpId(ampId, instrumentId, bandId);
    }
}
