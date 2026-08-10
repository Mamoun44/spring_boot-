package com.mamon.tasks.service;

import com.mamon.tasks.model.Task;
import com.mamon.tasks.model.TaskStatus;
import com.mamon.tasks.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    //private List<Task> tasks = new ArrayList<>();

    TaskRepository taskRepository;
    private Long nextId = 1L;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //private Long currentId = 1L;
    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void creatTask(Task task) {
        //task.setId(currentId++);
        //tasks.add(task);
        taskRepository.save(task);

    }

    @Override
    public Task getTaskById(Long id) {
/*
        for(Task task : tasks){
            if(task.getId().equals(id))
                return task;
        }
        return null;
*/
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteTaskById(Long id) {
/*
        for(Task task : tasks){
            if(task.getId().equals(id)){
                tasks.remove(task);
                return true;
            }
        }
        return false;
*/
        try {
            taskRepository.deleteById(id);
            return true;
        }catch (Exception e){return false;}
    }

    @Override
    public boolean updateTaskById(Long id, Task updatedTask) {

/*
        for(Task task : tasks){
            if(task.getId().equals(id)){
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription());
                task.setStatus(updatedTask.getStatus());
                task.setPriority(updatedTask.getPriority());
                return true;
            }

        }
        return false;
*/
        Optional<Task> TaskOptional = taskRepository.findById(id);
        if(TaskOptional.isPresent()){
            Task task = TaskOptional.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            task.setPriority(updatedTask.getPriority());
            taskRepository.save(task);
            return true;
        }
    return false;
    }

    @Override
    public boolean updateTaskStatus(Long id, TaskStatus status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(status);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

}
