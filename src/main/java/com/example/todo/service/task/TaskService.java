package com.example.todo.service.task;

import com.example.todo.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor  // Lombocのアノテーション (TaskRepositoryを引数にするコンストラクタが作成される
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskEntity> find(TaskSearchEntity searchEntity) {
        return taskRepository.select(searchEntity);
    }

    // taskIdからtaskEntityを返却
    public Optional<TaskEntity> findById(long taskId) {
        return taskRepository.selectByID(taskId);
    }

    /*
     * タスク作成
     * @Transactional トランザクション
     */
    @Transactional
    public void create(TaskEntity entity) {
        taskRepository.insert(entity);
    }

    /*
     * タスク更新
     * @Transactional トランザクション
     */
    @Transactional
    public void update(TaskEntity entity) {
        taskRepository.update(entity);
    }

    /*
     * タスクの削除
     * @Transactional トランザクション
     */
    @Transactional
    public void delete(long id) {
        taskRepository.delete(id);
    }
}
