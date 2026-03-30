package com.blackout.api.gig.application.service;

import com.blackout.api.gig.application.port.out.LoadVenuePort;
import com.blackout.api.gig.application.port.out.SaveVenuePort;
import com.blackout.api.gig.domain.Venue;
import com.blackout.api.gig.infrastructure.web.dto.CreateVenueRequest;
import com.blackout.api.gig.infrastructure.web.dto.UpdateVenueRequest;
import com.blackout.api.gig.infrastructure.web.dto.VenueResponse;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VenueApplicationService {

    private final LoadVenuePort loadVenue;
    private final SaveVenuePort saveVenue;

    public VenueApplicationService(LoadVenuePort loadVenue, SaveVenuePort saveVenue) {
        this.loadVenue = loadVenue;
        this.saveVenue = saveVenue;
    }

    public List<VenueResponse> findAll(String bandId) {
        return loadVenue.findAllByBandId(bandId).stream()
                .map(VenueResponse::from)
                .toList();
    }

    @Transactional
    public VenueResponse create(String bandId, CreateVenueRequest req) {
        Venue venue = new Venue(bandId, req.name());
        venue.setCity(req.city());
        venue.setAddress(req.address());
        venue.setWebsite(req.website());
        venue.setCapacity(req.capacity());
        venue.setBookingName(req.bookingName());
        venue.setBookingEmail(req.bookingEmail());
        venue.setBookingPhone(req.bookingPhone());
        venue.setNotes(req.notes());
        return VenueResponse.from(saveVenue.save(venue));
    }

    @Transactional
    public VenueResponse update(String bandId, String id, UpdateVenueRequest req) {
        Venue venue = findOwned(bandId, id);
        if (req.name() != null) venue.setName(req.name());
        venue.setCity(req.city());
        venue.setAddress(req.address());
        venue.setWebsite(req.website());
        venue.setCapacity(req.capacity());
        venue.setBookingName(req.bookingName());
        venue.setBookingEmail(req.bookingEmail());
        venue.setBookingPhone(req.bookingPhone());
        venue.setNotes(req.notes());
        return VenueResponse.from(saveVenue.save(venue));
    }

    @Transactional
    public void remove(String bandId, String id) {
        findOwned(bandId, id);
        saveVenue.deleteById(id);
    }

    private Venue findOwned(String bandId, String id) {
        return loadVenue.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }
}
