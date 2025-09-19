package com.example.todo.controller.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/* 存在しないタスクIDのリクエストを受けた際に新しい例外を作成する
    RuntimeExceptionを継承することで、例外クラスになる
    例外が発生した場合、ResponseStatus(HttpStatus.NOT_FOUND)で返却する
    →　TaskNotFoundExceptionの例外時は、HttpStatus.NOT_FOUND(404)を返却する
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException{

}
