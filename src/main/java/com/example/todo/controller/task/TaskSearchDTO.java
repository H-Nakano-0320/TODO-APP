package com.example.todo.controller.task;

import java.util.List;
import java.util.Optional;

public record TaskSearchDTO(
        String summary,
        List<String> statusList
) {
    public boolean isChecked(String status) {
        // statusListがnull出ない場合、statusListに含まれているかチェック
        return Optional.ofNullable(statusList)
                .map(l -> l.contains(status))
                .orElse(false);
    }
}
