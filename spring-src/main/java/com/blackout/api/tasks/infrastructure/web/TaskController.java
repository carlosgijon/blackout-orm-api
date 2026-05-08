package com.blackout.api.tasks.infrastructure.web;

import com.blackout.api.shared.infrastructure.security.BlackoutAuthentication;
import com.blackout.api.tasks.application.TaskService;
import com.blackout.api.tasks.domain.TaskStatus;
import com.blackout.api.tasks.infrastructure.web.dto.TaskDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskDto.TaskResponse> findAll(BlackoutAuthentication auth) {
        return service.findAll(auth.getBandId());
    }

    @GetMapping("/{id}")
    public TaskDto.TaskResponse get(BlackoutAuthentication auth, @PathVariable String id) {
        return service.get(auth.getBandId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto.TaskResponse create(BlackoutAuthentication auth,
                                       @Valid @RequestBody TaskDto.CreateTaskRequest req) {
        return service.create(auth.getBandId(), auth.getUserId(), req);
    }

    @PutMapping("/{id}")
    public TaskDto.TaskResponse update(BlackoutAuthentication auth,
                                       @PathVariable String id,
                                       @Valid @RequestBody TaskDto.UpdateTaskRequest req) {
        return service.update(auth.getBandId(), id, req);
    }

    @PatchMapping("/{id}/status")
    public TaskDto.TaskResponse setStatus(BlackoutAuthentication auth,
                                          @PathVariable String id,
                                          @RequestParam TaskStatus status) {
        return service.setStatus(auth.getBandId(), id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(BlackoutAuthentication auth, @PathVariable String id) {
        service.delete(auth.getBandId(), id);
    }
}
