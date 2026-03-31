package com.blackout.api.gig.domain;

import com.blackout.api.gig.domain.event.GigMarkedAsCobrado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pure unit tests for the Gig aggregate domain methods.
 * No Spring context — uses Mockito for ApplicationEventPublisher.
 */
class GigDomainTest {

    @Test
    @DisplayName("changeStatus to 'played' sets followUpDate to 1st of next month")
    void changeStatus_toPlayed_setsFollowUpDate() {
        Gig gig = new Gig("band-1", "Rock Night");
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);

        gig.changeStatus("played", events);

        LocalDate expected = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        assertThat(gig.getFollowUpDate()).isEqualTo(expected.toString());
        assertThat(gig.getFollowUpNote()).isEqualTo("Recordatorio: cobrar este concierto");
        assertThat(gig.getStatus()).isEqualTo("played");
        verifyNoInteractions(events);
    }

    @Test
    @DisplayName("changeStatus to 'played' again (already played) does NOT reset followUpDate")
    void changeStatus_toPlayed_whenAlreadyPlayed_noChange() {
        Gig gig = new Gig("band-1", "Rock Night");
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
        gig.changeStatus("played", events);
        String originalDate = gig.getFollowUpDate();

        // calling played again should not update the date
        gig.changeStatus("played", events);

        assertThat(gig.getFollowUpDate()).isEqualTo(originalDate);
    }

    @Test
    @DisplayName("changeStatus to 'cobrado' publishes GigMarkedAsCobrado event and clears followUp")
    void changeStatus_toCobrado_publishesEvent() {
        Gig gig = new Gig("band-1", "Rock Night");
        gig.setPay("500€");
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);

        // Set to played first so followUp data is populated
        gig.changeStatus("played", events);
        assertThat(gig.getFollowUpDate()).isNotNull();

        // Now mark as cobrado
        gig.changeStatus("cobrado", events);

        verify(events).publishEvent(any(GigMarkedAsCobrado.class));
        assertThat(gig.getStatus()).isEqualTo("cobrado");
        assertThat(gig.getFollowUpDate()).isNull();
        assertThat(gig.getFollowUpNote()).isNull();
    }

    @Test
    @DisplayName("changeStatus to 'cobrado' when already cobrado does NOT publish duplicate event")
    void changeStatus_toCobrado_whenAlreadyCobrado_noDuplicateEvent() {
        Gig gig = new Gig("band-1", "Rock Night");
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);

        gig.changeStatus("cobrado", events);
        verify(events, times(1)).publishEvent(any(GigMarkedAsCobrado.class));

        // Second call — should NOT publish again
        gig.changeStatus("cobrado", events);
        verify(events, times(1)).publishEvent(any(GigMarkedAsCobrado.class));
    }

    @Test
    @DisplayName("changeStatus to 'lead' from 'played' does not affect followUpDate")
    void changeStatus_toLead_fromPlayed_doesNotClearFollowUp() {
        Gig gig = new Gig("band-1", "Rock Night");
        ApplicationEventPublisher events = mock(ApplicationEventPublisher.class);
        gig.changeStatus("played", events);
        String originalDate = gig.getFollowUpDate();

        gig.changeStatus("lead", events);

        // followUpDate remains — changeStatus only modifies followUp on 'played' and 'cobrado'
        assertThat(gig.getFollowUpDate()).isEqualTo(originalDate);
        assertThat(gig.getStatus()).isEqualTo("lead");
    }

    @Test
    @DisplayName("GigMarkedAsCobrado event carries correct gig data")
    void cobradoEvent_carriesCorrectData() {
        Gig gig = new Gig("band-1", "Summer Fest");
        gig.setPay("800€");

        List<Object> publishedEvents = new ArrayList<>();
        ApplicationEventPublisher events = publishedEvents::add;

        gig.changeStatus("cobrado", events);

        assertThat(publishedEvents).hasSize(1);
        GigMarkedAsCobrado event = (GigMarkedAsCobrado) publishedEvents.get(0);
        assertThat(event.bandId()).isEqualTo("band-1");
        assertThat(event.title()).isEqualTo("Summer Fest");
        assertThat(event.pay()).isEqualTo("800€");
    }
}
