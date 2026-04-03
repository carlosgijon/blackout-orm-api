package com.blackout.api.mixer.application.service;

import com.blackout.api.mixer.application.port.out.LoadScnFilePort;
import com.blackout.api.mixer.application.port.out.SaveScnFilePort;
import com.blackout.api.mixer.domain.ScnFile;
import com.blackout.api.mixer.infrastructure.web.dto.ScnFileResponse;
import com.blackout.api.mixer.infrastructure.web.dto.SaveScnFileRequest;
import com.blackout.api.mixer.infrastructure.web.dto.UpdateScnFileRequest;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ScnFileApplicationService {

    private final LoadScnFilePort load;
    private final SaveScnFilePort save;

    public ScnFileApplicationService(LoadScnFilePort load, SaveScnFilePort save) {
        this.load = load;
        this.save = save;
    }

    public List<ScnFileResponse> findAll(String bandId) {
        return load.findAllByBandId(bandId).stream().map(this::toResponse).toList();
    }

    public ScnFileResponse get(String bandId, String id) {
        return toResponse(findOwned(bandId, id));
    }

    @Transactional
    public ScnFileResponse create(String bandId, SaveScnFileRequest req) {
        ScnFile file = new ScnFile(bandId, req.name(), req.content());
        file.setNotes(req.notes());
        file.setGigId(req.gigId());
        file.setVenue(req.venue());
        return toResponse(save.save(file));
    }

    @Transactional
    public ScnFileResponse update(String bandId, String id, UpdateScnFileRequest req) {
        ScnFile file = findOwned(bandId, id);
        if (req.name()  != null) file.setName(req.name());
        if (req.notes() != null) file.setNotes(req.notes());
        file.setGigId(req.gigId());
        file.setVenue(req.venue());
        file.setUpdatedAt(Instant.now());
        return toResponse(save.save(file));
    }

    @Transactional
    public void delete(String bandId, String id) {
        findOwned(bandId, id);
        save.deleteById(id);
    }

    private ScnFile findOwned(String bandId, String id) {
        return load.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo SCN no encontrado"));
    }

    private ScnFileResponse toResponse(ScnFile f) {
        return new ScnFileResponse(
                f.getId(), f.getBandId(), f.getName(), f.getContent(),
                f.getNotes(), f.getGigId(), f.getVenue(),
                f.getCreatedAt(), f.getUpdatedAt());
    }
}
