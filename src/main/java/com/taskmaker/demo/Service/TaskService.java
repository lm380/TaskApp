package com.taskmaker.demo.Service;

import com.taskmaker.demo.Repo.TaskRepo;
import com.taskmaker.demo.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    private final TaskRepo taskRepo;


    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> findAll(){
        return taskRepo.findAll();
    }

    public Optional<Task> findTaskById(Long id){
        return taskRepo.findById(id);
    }

    public Task saveTask(Task task){
        return taskRepo.save(task);
    }

    public void deleteById(Long id) {
         taskRepo.deleteById(id);
    }
}
