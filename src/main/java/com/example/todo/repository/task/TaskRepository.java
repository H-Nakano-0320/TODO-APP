package com.example.todo.repository.task;

import com.example.todo.service.task.TaskEntity;
import com.example.todo.service.task.TaskSearchEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper  // MyBatisのアノテーション (public classからinterfaceに変更する）
public interface TaskRepository {

    /*
     * 一覧表示・検索
     * 　　mybatisのDynamicSQL
     * 　　<script></script>の中でifやforが使用可能
     * 　　　空の場合はWHERE句を不要とする
     * 　　collection='condition.status' の中身を1件ずつ取得
     * 　　item='item'　取得した要素
     * 　　index='index'　itemのインデックス
     * 　　separator=','　取得した要素をカンマで結合
     */
    @Select("""
            <script>
                SELECT id, summary, description, status, create_at, update_at, due_date
                FROM tasks
                <where>
                    <if test='condition.summary != null and !condition.summary.isBlank()'>
                        summary LIKE CONCAT('%', #{condition.summary}, '%');
                    </if>
                    <if test='condition.status != null and !condition.status.isEmpty()'>
                        AND status IN (
                            <foreach item='item' index='index' collection='condition.status' separator=','>
                                #{item}
                            </foreach>
                        )
                    </if>
                </where>
            </script>
            """)
    List<TaskEntity> select(@Param("condition") TaskSearchEntity condition);

    /*
     * タスク詳細
     */
    @Select("""
            SELECT id , summary, description, status, create_at, update_at, due_date
            FROM tasks WHERE id = #{taskId}
            """)
    Optional<TaskEntity> selectByID(@Param("taskId") long taskId);


    /*
     * 登録処理
     */
    @Insert("""
            INSERT INTO tasks(summary, description, status, due_date)
            VALUES(#{task.summary}, #{task.description} , #{task.status}, #{task.due_date})
            """)
    // クエリにTaskEntityを使用したいため、"@Param()"を使用
    void insert(@Param("task") TaskEntity entity);

    /*
     * 更新処理
     */
    @Update("""
            UPDATE tasks
            SET
                summary      = #{task.summary}
                ,description = #{task.description}
                ,status      = #{task.status}
                ,update_at   = CURRENT_TIMESTAMP()
                ,due_date    = #{task.dueDate}
            WHERE
                id = #{task.id}
            """)
    void update(@Param("task") TaskEntity entity);

    /*
     * 削除
     */
    @Delete("DELETE FROM tasks WHERE id = #{taskId}")
    void delete(@Param("taskId") long id);
}
