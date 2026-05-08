package com.blackout.api.tasks.application;

import com.blackout.api.tasks.domain.Task;
import com.blackout.api.tasks.domain.TaskStatus;
import com.blackout.api.tasks.infrastructure.persistence.TaskRepository;
import com.blackout.api.tasks.infrastructure.web.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskDto.TaskResponse> findAll(String bandId) {
        return repository.findByBandIdOrderByCreatedAtDesc(bandId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public TaskDto.TaskResponse get(String bandId, String taskId) {
        return mapToDto(findTask(bandId, taskId));
    }

    public TaskDto.TaskResponse create(String bandId, String userId, TaskDto.CreateTaskRequest req) {
        Task task = new Task(bandId, req.title(), userId);
        task.setDescription(req.description());
        if (req.priority() != null) task.setPriority(req.priority());
        task.setAssigneeId(req.assigneeId());
        task.setDueDate(req.dueDate());

        return mapToDto(repository.save(task));
    }

    public TaskDto.TaskResponse update(String bandId, String taskId, TaskDto.UpdateTaskRequest req) {
        Task task = findTask(bandId, taskId);

        if (req.title() != null) task.setTitle(req.title());
        if (req.description() != null) task.setDescription(req.description());
        if (req.status() != null) task.setStatus(req.status());
        if (req.priority() != null) task.setPriority(req.priority());
        if (req.assigneeId() != null) task.setAssigneeId(req.assigneeId());
        if (req.dueDate() != null) task.setDueDate(req.dueDate());

        return mapToDto(repository.save(task));
    }

    public void delete(String bandId, String taskId) {
        Task task = findTask(bandId, taskId);
        repository.delete(task);
    }

    public TaskDto.TaskResponse setStatus(String bandId, String taskId, TaskStatus status) {
        Task task = findTask(bandId, taskId);
        task.setStatus(status);
        return mapToDto(repository.save(task));
    }

    private Task findTask(String bandId, String taskId) {
        Task task = repository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getBandId().equals(bandId)) {
            throw new RuntimeException("Not allowed");
        }
        return task;
    }

    private TaskDto.TaskResponse mapToDto(Task t) {
        return new TaskDto.TaskResponse(
                t.getId(), t.getBandId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getPriority(), t.getAssigneeId(),
                t.getCreatedBy(), t.getDueDate(), t.getCreatedAt()
        );
    }
}
