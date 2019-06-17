package com.taskmaker.demo.Controller;

import com.taskmaker.demo.Service.TaskService;
import com.taskmaker.demo.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Task>> findAll(){
        return ResponseEntity.ok(taskService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Task task){
        return ResponseEntity.ok(taskService.saveTask(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        Optional<Task> task = taskService.findTaskById(id);
        if(!task.isPresent()){
            log.error("Task with id: " + id + " could not be found");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(task.get());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody Task task){
        if(!taskService.findTaskById(id).isPresent()){
            log.error("Task with id: " + id + " could not be found");
            return ResponseEntity.badRequest().build();
        }
        task.setId(id);
        return ResponseEntity.ok(taskService.saveTask(task));
    }
}
