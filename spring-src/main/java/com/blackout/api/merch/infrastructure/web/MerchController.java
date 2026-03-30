package com.blackout.api.merch.infrastructure.web;

import com.blackout.api.merch.application.service.MerchApplicationService;
import com.blackout.api.merch.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merch")
public class MerchController {

    private final MerchApplicationService service;

    public MerchController(MerchApplicationService service) {
        this.service = service;
    }

    // IMPORTANT: /merch/waiting must be declared before /{id} to avoid path conflict

    @GetMapping("/waiting")
    public List<WaitingEntryResponse> getAllWaiting(BlackoutAuthentication auth) {
        return service.getAllWaiting(auth.getBandId());
    }

    @PutMapping("/waiting/{entryId}")
    public WaitingEntryResponse updateWaitingEntry(
            BlackoutAuthentication auth,
            @PathVariable String entryId,
            @RequestBody UpdateWaitingRequest dto) {
        return service.updateWaitingEntry(auth.getBandId(), entryId, dto);
    }

    @DeleteMapping("/waiting/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWaitingEntry(BlackoutAuthentication auth, @PathVariable String entryId) {
        service.removeWaitingEntry(auth.getBandId(), entryId);
    }

    @GetMapping
    public List<MerchItemResponse> findAll(BlackoutAuthentication auth) {
        return service.findAll(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MerchItemResponse create(
            BlackoutAuthentication auth,
            @Valid @RequestBody CreateMerchItemRequest dto) {
        return service.create(auth.getBandId(), dto);
    }

    @PutMapping("/{id}")
    public MerchItemResponse update(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody UpdateMerchItemRequest dto) {
        return service.update(auth.getBandId(), id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.remove(auth.getBandId(), id);
    }

    @PostMapping("/{id}/restock")
    public MerchItemResponse restock(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @RequestBody RestockRequest dto) {
        return service.restock(auth.getBandId(), id, dto);
    }

    @PostMapping("/{id}/sell")
    public SellResponse sell(
            BlackoutAuthentication auth,
            @PathVariable String id,
            @Valid @RequestBody SellRequest dto) {
        return service.sell(auth.getBandId(), id, dto);
    }

    @PostMapping("/{itemId}/waiting")
    @ResponseStatus(HttpStatus.CREATED)
    public WaitingEntryResponse addToWaiting(
            BlackoutAuthentication auth,
            @PathVariable String itemId,
            @Valid @RequestBody AddWaitingRequest dto) {
        return service.addToWaitingList(auth.getBandId(), itemId, dto);
    }
}
