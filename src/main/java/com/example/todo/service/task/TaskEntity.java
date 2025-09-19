package com.example.todo.service.task;

import java.time.LocalDateTime;

public record TaskEntity(
        Long id,
        String summary,             //概要
        String description,         //説明
        TaskStatus status,
        LocalDateTime create_at,
        LocalDateTime update_at,
        LocalDateTime due_date
) {
}
