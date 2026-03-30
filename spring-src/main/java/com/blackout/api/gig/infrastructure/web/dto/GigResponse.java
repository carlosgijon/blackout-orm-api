package com.blackout.api.gig.infrastructure.web.dto;

import com.blackout.api.gig.domain.Gig;
import java.time.Instant;

public record GigResponse(
        String id,
        String bandId,
        String venueId,
        String venueName,
        String setlistId,
        String title,
        String date,
        String time,
        String status,
        String pay,
        String loadInTime,
        String soundcheckTime,
        String setTime,
        String notes,
        Integer attendance,
        String followUpDate,
        String followUpNote,
        Instant createdAt) {

    public static GigResponse from(Gig gig, String venueName) {
        return new GigResponse(
                gig.getId(), gig.getBandId(), gig.getVenueId(), venueName,
                gig.getSetlistId(), gig.getTitle(), gig.getDate(), gig.getTime(),
                gig.getStatus(), gig.getPay(), gig.getLoadInTime(),
                gig.getSoundcheckTime(), gig.getSetTime(), gig.getNotes(),
                gig.getAttendance(), gig.getFollowUpDate(), gig.getFollowUpNote(),
                gig.getCreatedAt());
    }
}
