package com.mamon.tasks.controller;

import com.mamon.tasks.dto.TaskResponseDto;
import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(){
        return new ResponseEntity<>(taskService.getTasks(),HttpStatus.OK);
        //or return ResponseEntity.ok(taskService.getTasks())
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Task task = taskService.getTaskById(id);
        if (task != null){
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @PostMapping
    public ResponseEntity<String> creatTask (@RequestBody Task task){
        taskService.creatTask(task);
        return new ResponseEntity<>("Task added successfully",HttpStatus.CREATED    );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        boolean deleted = taskService.deleteTaskById(id);
        if (deleted)
            return new ResponseEntity<>("jop deleted successfully",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task updatedTask){
        boolean updated;
        updated = taskService.updateTaskById(id,updatedTask);
        if(updated)
            return new ResponseEntity<>("Task updated successfully",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        boolean updated = taskService.updateTaskStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Task status updated successfully");
        }
        return ResponseEntity.notFound().build();
    }


}
