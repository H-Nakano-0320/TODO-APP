package com.example.todo.controller.task;

import com.example.todo.service.task.TaskSearchEntity;
import com.example.todo.service.task.TaskStatus;

import java.util.List;
import java.util.Optional;

/*
 * summary : String型
 * status  : List<String>型　（複数選択可能なため）
 */
public record TaskSearchForm(
        String summary,
        List<String> status
) {
    public TaskSearchEntity toEntity(){
        // searchForm.statusのList<String>の値をenumに変換
        // Optional.ofNullable() nullが渡されたときは空のリストとして初期化する
        var statusEntityList = Optional.ofNullable(status())
                .map(statusList -> statusList.stream()
                        .map(TaskStatus::valueOf)
                        .toList()
                )
                .orElse(List.of());

        // Serviceで使用するTaskSearchEntityとしてインスタンス化
        return new TaskSearchEntity(summary(), statusEntityList);
    }

    public TaskSearchDTO toDTO() {
        return new TaskSearchDTO(summary(), status());
    }
}
