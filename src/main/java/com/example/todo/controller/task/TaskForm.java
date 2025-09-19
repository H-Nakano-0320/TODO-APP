package com.example.todo.controller.task;

import com.example.todo.service.task.TaskEntity;
import com.example.todo.service.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TaskForm(
        @NotBlank
        @Size(max = 256, message = "256文字以内で入力をしてください")
        String summary,
        String description,
        @NotBlank
        @Pattern(regexp = "TODO|DOING|DONE", message = "Todo, Doing, Doneのいずれかを選択してください。")
        String status,
        // ISO 8601形式（例: yyyy-MM-ddTHH:mm:ss）でフォーマット
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime due_date

) {


    /*
     * 更新フォームの初期表示
     */
    public static TaskForm fromEntity(TaskEntity taskEntity) {
        return  new TaskForm(
                taskEntity.summary(),
                taskEntity.description(),
                taskEntity.status().name(),
                taskEntity.due_date()
        );
    }

    /*
     * 登録
     */
    public TaskEntity toEntity() {
        // idはDBで自動裁判されるため、nullを指定
        // 作成日と更新日はSQLで書くため、nullを指定
        // valueOf：文字数のstatusから一致するenumクラスを取得
        return new TaskEntity(
                            null,
                                summary(),
                                description(),
                                TaskStatus.valueOf(status()),
                        null,
                        null,
                                due_date()
                            );
    }

    /*
     * 更新
     */
    public TaskEntity toEntity(long id) {
        // 作成日と更新日はSQLで書くため、nullを指定
        // valueOf：文字数のstatusから一致するenumクラスを取得
        return new TaskEntity(
                                id,
                                summary(),
                                description(),
                                TaskStatus.valueOf(status()),
                                null,
                                null,
                                due_date());
    }
}
