package com.blackout.api.equipment.application.service;

import com.blackout.api.equipment.application.port.out.LoadEquipmentPort;
import com.blackout.api.equipment.application.port.out.SaveEquipmentPort;
import com.blackout.api.equipment.domain.*;
import com.blackout.api.equipment.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EquipmentApplicationService {

    private final LoadEquipmentPort loadPort;
    private final SaveEquipmentPort savePort;
    private final ObjectMapper objectMapper;

    public EquipmentApplicationService(LoadEquipmentPort loadPort,
                                       SaveEquipmentPort savePort,
                                       ObjectMapper objectMapper) {
        this.loadPort = loadPort;
        this.savePort = savePort;
        this.objectMapper = objectMapper;
    }

    // ── BandMembers ───────────────────────────────────────────────────────────

    public List<MemberResponse> findAllMembers(String bandId) {
        return loadPort.findAllMembersByBandId(bandId).stream()
                .map(m -> MemberResponse.from(m, objectMapper))
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponse createMember(String bandId, CreateMemberRequest req) {
        BandMember member = new BandMember(bandId, req.name());
        applyMemberFields(member, req.roles(), req.stagePosition(), req.vocalMicId(),
                req.notes(), req.sortOrder());
        return MemberResponse.from(savePort.saveMember(member), objectMapper);
    }

    @Transactional
    public MemberResponse updateMember(String bandId, String id, UpdateMemberRequest req) {
        BandMember member = loadPort.findMemberByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("BandMember not found: " + id));
        if (req.name() != null) member.setName(req.name());
        if (req.roles() != null) {
            try {
                member.setRoles(objectMapper.writeValueAsString(req.roles()));
            } catch (Exception e) {
                member.setRoles("[]");
            }
        }
        if (req.stagePosition() != null) member.setStagePosition(req.stagePosition());
        if (req.vocalMicId() != null) member.setVocalMicId(req.vocalMicId());
        if (req.notes() != null) member.setNotes(req.notes());
        if (req.sortOrder() != null) member.setSortOrder(req.sortOrder());
        return MemberResponse.from(savePort.saveMember(member), objectMapper);
    }

    @Transactional
    public void removeMember(String bandId, String id) {
        loadPort.findMemberByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("BandMember not found: " + id));
        savePort.deleteMemberById(id);
    }

    private void applyMemberFields(BandMember member, List<String> roles,
                                   String stagePosition, String vocalMicId,
                                   String notes, int sortOrder) {
        try {
            member.setRoles(roles != null ? objectMapper.writeValueAsString(roles) : "[]");
        } catch (Exception e) {
            member.setRoles("[]");
        }
        member.setStagePosition(stagePosition);
        member.setVocalMicId(vocalMicId);
        member.setNotes(notes);
        member.setSortOrder(sortOrder);
    }

    // ── Instruments ───────────────────────────────────────────────────────────

    public List<InstrumentResponse> findAllInstruments(String bandId) {
        return loadPort.findAllInstrumentsByBandId(bandId).stream()
                .map(InstrumentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstrumentResponse createInstrument(String bandId, CreateInstrumentRequest req) {
        Instrument instrument = new Instrument(bandId, req.name(), req.type(),
                req.routing() != null ? req.routing() : "direct");
        instrument.setBrand(req.brand());
        instrument.setModel(req.model());
        instrument.setMemberId(req.memberId());
        instrument.setAmpId(req.ampId());
        instrument.setChannelOrder(req.channelOrder());
        instrument.setMonoStereo(req.monoStereo());
        instrument.setNotes(req.notes());
        return InstrumentResponse.from(savePort.saveInstrument(instrument));
    }

    @Transactional
    public InstrumentResponse updateInstrument(String bandId, String id, UpdateInstrumentRequest req) {
        Instrument instrument = loadPort.findInstrumentByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + id));
        if (req.name() != null) instrument.setName(req.name());
        if (req.type() != null) instrument.setType(req.type());
        if (req.brand() != null) instrument.setBrand(req.brand());
        if (req.model() != null) instrument.setModel(req.model());
        if (req.routing() != null) instrument.setRouting(req.routing());
        if (req.memberId() != null) instrument.setMemberId(req.memberId());
        if (req.ampId() != null) instrument.setAmpId(req.ampId());
        if (req.channelOrder() != null) instrument.setChannelOrder(req.channelOrder());
        if (req.monoStereo() != null) instrument.setMonoStereo(req.monoStereo());
        if (req.notes() != null) instrument.setNotes(req.notes());
        return InstrumentResponse.from(savePort.saveInstrument(instrument));
    }

    @Transactional
    public void removeInstrument(String bandId, String id) {
        loadPort.findInstrumentByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + id));
        savePort.deleteInstrumentById(id);
    }

    @Transactional
    public void setInstrumentMics(String bandId, String instrumentId, List<String> micIds) {
        loadPort.findInstrumentByIdAndBandId(instrumentId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrument not found: " + instrumentId));
        savePort.clearMicAssignment(bandId, "instrument", instrumentId);
        if (micIds != null && !micIds.isEmpty()) {
            savePort.bulkAssignMics(bandId, "instrument", instrumentId, micIds);
        }
    }

    // ── Amplifiers ────────────────────────────────────────────────────────────

    public List<AmplifierResponse> findAllAmplifiers(String bandId) {
        return loadPort.findAllAmplifiersByBandId(bandId).stream()
                .map(AmplifierResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public AmplifierResponse createAmplifier(String bandId, CreateAmplifierRequest req) {
        Amplifier amplifier = new Amplifier(bandId, req.name(), req.type(),
                req.routing() != null ? req.routing() : "direct");
        amplifier.setBrand(req.brand());
        amplifier.setModel(req.model());
        amplifier.setWattage(req.wattage());
        amplifier.setMonoStereo(req.monoStereo());
        amplifier.setStagePosition(req.stagePosition());
        amplifier.setMemberId(req.memberId());
        amplifier.setCabinetBrand(req.cabinetBrand());
        amplifier.setSpeakerBrand(req.speakerBrand());
        amplifier.setSpeakerModel(req.speakerModel());
        amplifier.setSpeakerConfig(req.speakerConfig());
        amplifier.setNotes(req.notes());
        return AmplifierResponse.from(savePort.saveAmplifier(amplifier));
    }

    @Transactional
    public AmplifierResponse updateAmplifier(String bandId, String id, UpdateAmplifierRequest req) {
        Amplifier amplifier = loadPort.findAmplifierByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Amplifier not found: " + id));
        if (req.name() != null) amplifier.setName(req.name());
        if (req.type() != null) amplifier.setType(req.type());
        if (req.brand() != null) amplifier.setBrand(req.brand());
        if (req.model() != null) amplifier.setModel(req.model());
        if (req.wattage() != null) amplifier.setWattage(req.wattage());
        if (req.routing() != null) amplifier.setRouting(req.routing());
        if (req.monoStereo() != null) amplifier.setMonoStereo(req.monoStereo());
        if (req.stagePosition() != null) amplifier.setStagePosition(req.stagePosition());
        if (req.memberId() != null) amplifier.setMemberId(req.memberId());
        if (req.cabinetBrand() != null) amplifier.setCabinetBrand(req.cabinetBrand());
        if (req.speakerBrand() != null) amplifier.setSpeakerBrand(req.speakerBrand());
        if (req.speakerModel() != null) amplifier.setSpeakerModel(req.speakerModel());
        if (req.speakerConfig() != null) amplifier.setSpeakerConfig(req.speakerConfig());
        if (req.notes() != null) amplifier.setNotes(req.notes());
        return AmplifierResponse.from(savePort.saveAmplifier(amplifier));
    }

    @Transactional
    public void removeAmplifier(String bandId, String id) {
        loadPort.findAmplifierByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Amplifier not found: " + id));
        savePort.deleteAmplifierById(id);
    }

    @Transactional
    public void updateInstrumentLink(String bandId, String ampId, String instrumentId) {
        loadPort.findAmplifierByIdAndBandId(ampId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Amplifier not found: " + ampId));
        // Clear old instrument link for this amp
        savePort.clearInstrumentAmpLink(ampId, bandId);
        // Set new link if instrumentId provided
        if (instrumentId != null && !instrumentId.isBlank()) {
            savePort.setInstrumentAmpLink(ampId, instrumentId, bandId);
        }
    }

    @Transactional
    public void setAmplifierMics(String bandId, String amplifierId, List<String> micIds) {
        loadPort.findAmplifierByIdAndBandId(amplifierId, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Amplifier not found: " + amplifierId));
        savePort.clearMicAssignment(bandId, "amplifier", amplifierId);
        if (micIds != null && !micIds.isEmpty()) {
            savePort.bulkAssignMics(bandId, "amplifier", amplifierId, micIds);
        }
    }

    // ── Microphones ───────────────────────────────────────────────────────────

    public List<MicrophoneResponse> findAllMicrophones(String bandId) {
        return loadPort.findAllMicrophonesByBandId(bandId).stream()
                .map(MicrophoneResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MicrophoneResponse createMicrophone(String bandId, CreateMicrophoneRequest req) {
        Microphone mic = new Microphone(bandId, req.name(), req.type() != null ? req.type() : "");
        mic.setBrand(req.brand());
        mic.setModel(req.model());
        mic.setType(req.type());
        mic.setPolarPattern(req.polarPattern());
        mic.setMonoStereo(req.monoStereo() != null ? req.monoStereo() : "mono");
        mic.setPhantomPower(req.phantomPower());
        mic.setAssignedToType(req.assignedToType());
        mic.setAssignedToId(req.assignedToId());
        mic.setUsage(req.usage());
        mic.setNotes(req.notes());
        return MicrophoneResponse.from(savePort.saveMicrophone(mic));
    }

    @Transactional
    public MicrophoneResponse updateMicrophone(String bandId, String id, UpdateMicrophoneRequest req) {
        Microphone mic = loadPort.findMicrophoneByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Microphone not found: " + id));
        if (req.name() != null) mic.setName(req.name());
        if (req.brand() != null) mic.setBrand(req.brand());
        if (req.model() != null) mic.setModel(req.model());
        if (req.type() != null) mic.setType(req.type());
        if (req.polarPattern() != null) mic.setPolarPattern(req.polarPattern());
        if (req.monoStereo() != null) mic.setMonoStereo(req.monoStereo());
        if (req.phantomPower() != null) mic.setPhantomPower(req.phantomPower());
        if (req.assignedToType() != null) mic.setAssignedToType(req.assignedToType());
        if (req.assignedToId() != null) mic.setAssignedToId(req.assignedToId());
        if (req.usage() != null) mic.setUsage(req.usage());
        if (req.notes() != null) mic.setNotes(req.notes());
        return MicrophoneResponse.from(savePort.saveMicrophone(mic));
    }

    @Transactional
    public void removeMicrophone(String bandId, String id) {
        loadPort.findMicrophoneByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Microphone not found: " + id));
        savePort.deleteMicrophoneById(id);
    }

    // ── PA Equipment ──────────────────────────────────────────────────────────

    public List<PaEquipmentResponse> findAllPa(String bandId) {
        return loadPort.findAllPaByBandId(bandId).stream()
                .map(PaEquipmentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaEquipmentResponse createPa(String bandId, CreatePaRequest req) {
        PaEquipment pa = new PaEquipment(bandId,
                req.category() != null ? req.category() : "other", req.name());
        pa.setBrand(req.brand());
        pa.setModel(req.model());
        pa.setQuantity(req.quantity() > 0 ? req.quantity() : 1);
        pa.setChannels(req.channels());
        pa.setAuxSends(req.auxSends());
        pa.setWattage(req.wattage());
        pa.setMonitorType(req.monitorType());
        pa.setIemWireless(req.iemWireless());
        pa.setNotes(req.notes());
        return PaEquipmentResponse.from(savePort.savePa(pa));
    }

    @Transactional
    public PaEquipmentResponse updatePa(String bandId, String id, UpdatePaRequest req) {
        PaEquipment pa = loadPort.findPaByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("PaEquipment not found: " + id));
        if (req.name() != null) pa.setName(req.name());
        if (req.category() != null) pa.setCategory(req.category());
        if (req.brand() != null) pa.setBrand(req.brand());
        if (req.model() != null) pa.setModel(req.model());
        if (req.quantity() != null) pa.setQuantity(req.quantity());
        if (req.channels() != null) pa.setChannels(req.channels());
        if (req.auxSends() != null) pa.setAuxSends(req.auxSends());
        if (req.wattage() != null) pa.setWattage(req.wattage());
        if (req.monitorType() != null) pa.setMonitorType(req.monitorType());
        if (req.iemWireless() != null) pa.setIemWireless(req.iemWireless());
        if (req.notes() != null) pa.setNotes(req.notes());
        return PaEquipmentResponse.from(savePort.savePa(pa));
    }

    @Transactional
    public void removePa(String bandId, String id) {
        loadPort.findPaByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("PaEquipment not found: " + id));
        savePort.deletePaById(id);
    }

    // ── Channel List ──────────────────────────────────────────────────────────

    public List<ChannelEntryResponse> generateChannelList(String bandId) {
        List<BandMember> members = loadPort.findAllMembersByBandId(bandId);
        List<Instrument> instruments = loadPort.findAllInstrumentsByBandId(bandId);
        List<Microphone> microphones = loadPort.findAllMicrophonesByBandId(bandId);

        Map<String, BandMember> memberMap = members.stream()
                .collect(Collectors.toMap(BandMember::getId, m -> m));
        Map<String, Microphone> micMap = microphones.stream()
                .collect(Collectors.toMap(Microphone::getId, m -> m));

        List<ChannelEntryResponse> channels = new ArrayList<>();
        int[] ch = {1};

        // Vocal mics for members
        for (BandMember member : members) {
            if (member.getVocalMicId() != null) {
                Microphone mic = micMap.get(member.getVocalMicId());
                String micModel = null;
                String micType = null;
                String polarPattern = null;
                String monoStereo = "mono";
                boolean phantomPower = false;

                if (mic != null) {
                    monoStereo = mic.getMonoStereo() != null ? mic.getMonoStereo() : "mono";
                    phantomPower = mic.isPhantomPower();
                    String brand = mic.getBrand() != null ? mic.getBrand() : "";
                    String model = mic.getModel() != null ? mic.getModel() : "";
                    String combined = (brand + " " + model).trim();
                    micModel = !combined.isBlank() ? combined : mic.getName();
                    micType = mic.getType();
                    polarPattern = mic.getPolarPattern();
                }

                channels.add(new ChannelEntryResponse(
                        ch[0]++,
                        member.getName() + " \u2013 Voz",
                        monoStereo,
                        phantomPower,
                        micModel,
                        micType,
                        polarPattern,
                        null,
                        member.getId()));
            }
        }

        // Drum usage labels
        Map<String, String> drumUsageLabel = Map.of(
                "drums-kick", "Bombo",
                "drums-overhead", "A\u00e9reos",
                "drums-snare", "Caja");

        // Instruments
        for (Instrument inst : instruments) {
            String memberName = inst.getMemberId() != null && memberMap.containsKey(inst.getMemberId())
                    ? memberMap.get(inst.getMemberId()).getName()
                    : null;

            List<Microphone> instMics = microphones.stream()
                    .filter(m -> inst.getId().equals(m.getAssignedToId())
                            && "instrument".equals(m.getAssignedToType()))
                    .collect(Collectors.toList());

            if (instMics.isEmpty()) {
                String entryName = memberName != null
                        ? memberName + " \u2013 " + inst.getName()
                        : inst.getName();
                String monoStereo = inst.getMonoStereo() != null ? inst.getMonoStereo() : "mono";
                channels.add(new ChannelEntryResponse(
                        ch[0]++,
                        entryName,
                        monoStereo,
                        false,
                        null, null, null, null,
                        inst.getMemberId()));
            } else {
                for (Microphone mic : instMics) {
                    String suffix = instMics.size() > 1
                            ? drumUsageLabel.getOrDefault(mic.getUsage(), inst.getName())
                            : inst.getName();
                    String entryName = memberName != null
                            ? memberName + " \u2013 " + suffix
                            : suffix;
                    String monoStereo = mic.getMonoStereo() != null ? mic.getMonoStereo() : "mono";
                    String brand = mic.getBrand() != null ? mic.getBrand() : "";
                    String model = mic.getModel() != null ? mic.getModel() : "";
                    String combined = (brand + " " + model).trim();
                    String micModel = !combined.isBlank() ? combined : mic.getName();

                    channels.add(new ChannelEntryResponse(
                            ch[0]++,
                            entryName,
                            monoStereo,
                            mic.isPhantomPower(),
                            micModel,
                            mic.getType(),
                            mic.getPolarPattern(),
                            null,
                            inst.getMemberId()));
                }
            }
        }

        return channels;
    }
}
