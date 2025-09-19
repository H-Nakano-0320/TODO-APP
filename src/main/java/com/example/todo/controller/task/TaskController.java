package com.example.todo.controller.task;

import com.example.todo.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    // インスタンス化
    private final TaskService taskService;

    /*
     * タスク一覧
     * GET  /tasks
     */
    @GetMapping     // templateフォルダ
    public String list(TaskSearchForm searchForm, Model model) {

        // List<TaskEntity> -> List<TaskDTO>
        var taskList = taskService.find(searchForm.toEntity())
                .stream()
                // .map(entity -> TaskDTO.toDTO(entity)のメソッド参照
                .map(TaskDTO::toDTO)
                .toList();

        // viewにkey,valueを渡す
        // タスク一覧用
        model.addAttribute("taskList", taskList);
        // 検索結果保持用
        model.addAttribute("searchDTO", searchForm.toDTO());

        return "tasks/list";    //list.html
    }

    /*
     * タスク詳細
     * Get  /tasks/{id}
     */
    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long taskId, Model model) {

        var taskDTO = taskService.findById(taskId)
                .map(TaskDTO::toDTO)
                .orElseThrow(TaskNotFoundException::new);

        // 35行目のDB検索結果のtaskEntityをtaskDTOに変換しViewに受け渡す
        // key:task value:taskDTO
        model.addAttribute("task", taskDTO);

        return "tasks/detail";
    }

    /*
     * タスク新規作成画面
     * GET /tasks/creationForm
     */
    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form, Model model) {
        model.addAttribute("mode", "CREATE");
        return "tasks/form";
    }

    /*
     * タスク新規作成ボタン押下時
     * POST /tasks
     */
    @PostMapping
    public String create(@Validated TaskForm form , BindingResult bindingResult, Model model) {
        // バリデーションエラーの判定
        if(bindingResult.hasErrors()){
            // 入力情報ほ保持したままにする
            return showCreationForm(form, model);
        }
        // 入力情報をDBに登録
        taskService.create(form.toEntity());

        // 二重サブミット対策
        return "redirect:/tasks";
    }

    /*
     * タスク編集画面
     * GET  /tasks/{taskId}/editForm
     * {id}のデータを抽出してViewに返却する
     */
    @GetMapping("/{id}/editForm")
    // @PathVariable("id") ＝　{id}　のこと
    public String showEditForm(@PathVariable("id") long id, Model model){
        /*
        ①`taskService.findById(id)` で `Optional<TaskEntity>` 型が取得できる
　　　　　　※取得不可の場合は、②、③がスキップ
　　　　　②`.map()` で`Optional<TaskEntity>` の中身を変換できる
        ③変換メソッドは`TaskForm.*fromEntity*(entity)` を指定しているため、中身が `Optional<TaskForm>`型になる
        ④`Optional<TaskForm>` 型を取得するために`.orElseThrow()`
        ⑤指定したidのレコードが存在する場合は、 `TaskForm` 型が `form` 変数に代入
        　存在しない場合は、`TaskNotFoundException` がThrowされる
         */
        var form = taskService.findById(id)
                        .map(TaskForm::fromEntity)
                        .orElseThrow(TaskNotFoundException::new);

        // key : taskFormでViewに渡す
        model.addAttribute("taskForm", form);
        model.addAttribute("mode","EDIT");
        return "tasks/form";
    }

    /*
     * 更新ボタン押下時のハンドラーメソッド
     * PUT  /tasks/{id}
     */
    @PutMapping("{id}")
    public String update(@PathVariable("id") long id, @Validated @ModelAttribute TaskForm form , BindingResult bindingResult, Model model){
        // バリデーションチェック
        if(bindingResult.hasErrors()){
            // 入力情報の保持 @ModelAttribute・・・引数なしは、先頭小文字(taskForm)のkey名でmodel.addAttributeされる
            // 再度編集画面に戻す
            model.addAttribute("mode", "EDIT");
            // tasks/formに戻す
            return "tasks/form";
        }
        // TaskFormの値をentityに変換する
        var entity = form.toEntity(id);
        taskService.update(entity);
        return "redirect:/tasks/{id}";
    }

    /*
     * 削除ボタン押下時
     * 　POST  /tasks/{id}  (hidden: _method: delete)
     * 　-> DELETE /tasks/{id}に変換
     */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id){
        taskService.delete(id);
        return "redirect:/tasks";
    }

}
