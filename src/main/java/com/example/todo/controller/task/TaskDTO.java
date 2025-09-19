package com.example.todo.controller.task;

import com.example.todo.service.task.TaskEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public record TaskDTO(
        long id,
        String summary,             //概要
        String description,         //説明
        String status,               //状態
        String last_updated,
        LocalDateTime create_at,
        LocalDateTime update_at,
        String due_date,
        String remaining_days
) {
    // staticメソッドで定義されている
    public static TaskDTO toDTO(TaskEntity entity) {

        /*
         * 定数
         */
        // 最終更新日のフォーマット
        final DateTimeFormatter DTF_LAST_UPDATED = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        // 期限のフォーマット
        final DateTimeFormatter DTF_DUE_DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        /*
         * 変数
         */
        // 期限までの残日数計算
        String remainingDays;


        // 最終更新日の取得とフォーマット
        LocalDateTime lastUpdated = entity.update_at() != null ?
                entity.update_at() : entity.create_at();
        String formattedDate = lastUpdated.format(DTF_LAST_UPDATED);

        // 期限日のフォーマット
        String formatDueDate = entity.due_date() != null ?
                entity.due_date().format(DTF_DUE_DATE) : "-";

        // 期限までの残日数計算
        if (entity.due_date() != null){
            // 現在日時の取得とフォーマット
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, entity.due_date());
            remainingDays = Long.toString(days);
        } else {
            remainingDays = "-";
        }

        return new TaskDTO(
                entity.id(),
                entity.summary(),
                entity.description(),
                entity.status().name(),
                formattedDate,
                entity.create_at(),
                entity.update_at(),
                formatDueDate,
                remainingDays
        );
    }

}
