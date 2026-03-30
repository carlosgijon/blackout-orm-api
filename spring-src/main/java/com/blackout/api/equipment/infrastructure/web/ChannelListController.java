package com.blackout.api.equipment.infrastructure.web;

import com.blackout.api.equipment.application.service.EquipmentApplicationService;
import com.blackout.api.equipment.infrastructure.web.dto.ChannelEntryResponse;
import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channel-list")
public class ChannelListController {

    private final EquipmentApplicationService service;

    public ChannelListController(EquipmentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ChannelEntryResponse> generate(BlackoutAuthentication auth) {
        return service.generateChannelList(auth.getBandId());
    }
}
