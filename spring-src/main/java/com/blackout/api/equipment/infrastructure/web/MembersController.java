package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.application.service.EquipmentApplicationService;
import com.blackout.api.equipment.infrastructure.web.dto.*;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MembersController {

    private final EquipmentApplicationService service;

    public MembersController(EquipmentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<MemberResponse> findAll(BlackoutAuthentication auth) {
        return service.findAllMembers(auth.getBandId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(BlackoutAuthentication auth,
                                 @Valid @RequestBody CreateMemberRequest req) {
        return service.createMember(auth.getBandId(), req);
    }

    @PutMapping("/{id}")
    public MemberResponse update(BlackoutAuthentication auth,
                                 @PathVariable String id,
                                 @RequestBody UpdateMemberRequest req) {
        return service.updateMember(auth.getBandId(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(BlackoutAuthentication auth, @PathVariable String id) {
        service.removeMember(auth.getBandId(), id);
    }
}
