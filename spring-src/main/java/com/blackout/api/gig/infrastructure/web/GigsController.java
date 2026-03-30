package com.blackout.api.gig.infrastructure.web;

import com.blackout.api.gig.application.service.GigApplicationService;
import com.blackout.api.gig.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gigs")
public class GigsController {

    private final GigApplicationService service;

    public GigsController(GigApplicationService service) {
        this.service = service;
    }

    // ── Gigs ─────────────────────────────────────────────────────────────────

    @GetMapping
    public List<GigResponse> findAll(BlackoutAuthentication auth) {
        requireBand(auth);
        return service.findAll(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GigResponse create(@RequestBody CreateGigRequest req, BlackoutAuthentication auth) {
        requireBand(auth);
        return service.create(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public GigResponse update(@PathVariable String id,
                               @RequestBody UpdateGigRequest req,
                               BlackoutAuthentication auth) {
        requireBand(auth);
        return service.update(auth.getBandId(), id, req);
    }

    @PutMapping("/{id}/status")
    public GigResponse updateStatus(@PathVariable String id,
                                     @RequestBody UpdateStatusRequest req,
                                     BlackoutAuthentication auth) {
        requireBand(auth);
        return service.updateStatus(auth.getBandId(), id, req.status());
    }

    @PutMapping("/{id}/follow-up")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFollowUp(@PathVariable String id,
                                @RequestBody UpdateFollowUpRequest req,
                                BlackoutAuthentication auth) {
        requireBand(auth);
        service.updateFollowUp(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String id, BlackoutAuthentication auth) {
        requireBand(auth);
        service.remove(auth.getBandId(), id);
    }

    // ── Summary — declared before /{id}/contacts to avoid ambiguity ──────────

    @GetMapping("/{id}/summary")
    public GigSummaryResponse getSummary(@PathVariable String id, BlackoutAuthentication auth) {
        requireBand(auth);
        return service.getSummary(auth.getBandId(), id);
    }

    // ── Contacts ─────────────────────────────────────────────────────────────

    @GetMapping("/{gigId}/contacts")
    public List<GigContactResponse> getContacts(@PathVariable String gigId,
                                                  BlackoutAuthentication auth) {
        requireBand(auth);
        return service.getContacts(auth.getBandId(), gigId);
    }

    @PostMapping("/{gigId}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    public GigContactResponse createContact(@PathVariable String gigId,
                                             @RequestBody CreateGigContactRequest req,
                                             BlackoutAuthentication auth) {
        requireBand(auth);
        return service.createContact(auth.getBandId(), gigId, req);
    }

    @DeleteMapping("/{gigId}/contacts/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContact(@PathVariable String gigId,
                               @PathVariable String contactId,
                               BlackoutAuthentication auth) {
        requireBand(auth);
        service.removeContact(auth.getBandId(), gigId, contactId);
    }

    // ── Checklists ───────────────────────────────────────────────────────────

    @GetMapping("/{gigId}/checklists")
    public List<GigChecklistResponse> getChecklists(@PathVariable String gigId,
                                                     BlackoutAuthentication auth) {
        requireBand(auth);
        return service.getChecklists(auth.getBandId(), gigId);
    }

    @PostMapping("/{gigId}/checklists")
    @ResponseStatus(HttpStatus.CREATED)
    public GigChecklistResponse createChecklist(@PathVariable String gigId,
                                                 @RequestBody CreateChecklistRequest req,
                                                 BlackoutAuthentication auth) {
        requireBand(auth);
        return service.createChecklist(auth.getBandId(), gigId, req);
    }

    @DeleteMapping("/{gigId}/checklists/{checklistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeChecklist(@PathVariable String gigId,
                                 @PathVariable String checklistId,
                                 BlackoutAuthentication auth) {
        requireBand(auth);
        service.removeChecklist(auth.getBandId(), gigId, checklistId);
    }

    // ── Checklist Items — static prefix /checklists must come before /{id} ───

    @GetMapping("/checklists/{checklistId}/items")
    public List<ChecklistItemResponse> getItems(@PathVariable String checklistId,
                                                 BlackoutAuthentication auth) {
        requireBand(auth);
        return service.getItems(auth.getBandId(), checklistId);
    }

    @PostMapping("/checklists/{checklistId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ChecklistItemResponse createItem(@PathVariable String checklistId,
                                             @RequestBody CreateChecklistItemRequest req,
                                             BlackoutAuthentication auth) {
        requireBand(auth);
        return service.createItem(auth.getBandId(), checklistId, req);
    }

    @PutMapping("/checklists/{checklistId}/items/{itemId}")
    public ChecklistItemResponse updateItem(@PathVariable String checklistId,
                                             @PathVariable String itemId,
                                             @RequestBody UpdateChecklistItemRequest req,
                                             BlackoutAuthentication auth) {
        requireBand(auth);
        return service.updateItem(auth.getBandId(), checklistId, itemId, req);
    }

    @DeleteMapping("/checklists/{checklistId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String checklistId,
                            @PathVariable String itemId,
                            BlackoutAuthentication auth) {
        requireBand(auth);
        service.removeItem(auth.getBandId(), checklistId, itemId);
    }

    @PostMapping("/checklists/{checklistId}/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetItems(@PathVariable String checklistId, BlackoutAuthentication auth) {
        requireBand(auth);
        service.resetItems(auth.getBandId(), checklistId);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private void requireBand(BlackoutAuthentication auth) {
        if (auth.getBandId() == null) throw new ForbiddenException("Token sin banda seleccionada.");
    }
}
